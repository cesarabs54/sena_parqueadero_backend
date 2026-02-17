package co.edu.sena.usecase.access;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.model.access.AccessLog.AccessType;
import co.edu.sena.model.access.VehicleStatus;
import co.edu.sena.model.access.gateways.AccessLogRepository;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record AccessUseCase(AccessLogRepository accessLogRepository,
                            AuthorizedVehicleRepository authorizedVehicleRepository,
                            ParkingLotRepository parkingLotRepository) {

    public Mono<AccessLog> registerEntry(String plate, UUID parkingLotId) {
        LocalDateTime now = LocalDateTime.now();
        return parkingLotRepository.findById(parkingLotId)
                .switchIfEmpty(Mono.error(new RuntimeException("Parking lot not found")))
                .flatMap(unused -> authorizedVehicleRepository.findByPlate(plate))
                .flatMap(vehicle -> {
                    if (Boolean.FALSE.equals(vehicle.getIsActive())) {
                        return Mono.error(new RuntimeException("Vehicle authorized but inactive"));
                    }
                    return createAccessLog(plate, parkingLotId, AccessType.ENTRY);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (isWeekend(now)) {
                        // Allow non-authorized vehicles on weekends (example rule)
                        return createAccessLog(plate, parkingLotId, AccessType.ENTRY);
                    } else {
                        return Mono.error(
                                new RuntimeException("Vehicle not authorized for weekday access"));
                    }
                }));
    }

    private boolean isWeekend(LocalDateTime dateTime) {
        var day = dateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public Mono<AccessLog> registerExit(String plate, UUID parkingLotId) {
        return createAccessLog(plate, parkingLotId, AccessType.EXIT);
    }

    private Mono<AccessLog> createAccessLog(String plate, UUID parkingLotId,
            AccessType type) {
        return accessLogRepository.create(AccessLog.builder()
                .id(UUID.randomUUID())
                .plate(plate)
                .timestamp(LocalDateTime.now())
                .type(type)
                .parkingLotId(parkingLotId)
                .build());
    }

    public Mono<Long> getOccupancy(UUID parkingLotId) {
        return accessLogRepository.countByParkingLotIdAndType(parkingLotId, AccessType.ENTRY)
                .zipWith(accessLogRepository.countByParkingLotIdAndType(parkingLotId,
                        AccessType.EXIT))
                .map(tuple -> tuple.getT1() - tuple.getT2());
    }

    public Flux<AccessLog> getAllAccessLogs() {
        return accessLogRepository.findAll();
    }

    public Mono<VehicleStatus> getVehicleStatus(String plate) {
        return accessLogRepository.findLastByPlate(plate)
                .map(log -> log.getType() == AccessType.ENTRY ? "IN" : "OUT")
                .defaultIfEmpty("OUT")
                .zipWith(authorizedVehicleRepository.findByPlate(plate)
                        .map(v -> Map.entry(true, v.getOwnerName()))
                        .defaultIfEmpty(Map.entry(false, "Visitante")))
                .map(tuple -> VehicleStatus.builder()
                        .plate(plate)
                        .currentStatus(tuple.getT1())
                        .isAuthorized(tuple.getT2().getKey())
                        .ownerName(tuple.getT2().getValue())
                        .build());
    }
}
