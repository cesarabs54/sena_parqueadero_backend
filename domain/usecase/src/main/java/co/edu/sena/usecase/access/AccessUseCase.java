package co.edu.sena.usecase.access;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.model.access.AccessLog.AccessType;
import co.edu.sena.model.access.VehicleStatus;
import co.edu.sena.model.access.gateways.AccessLogRepository;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record AccessUseCase(AccessLogRepository accessLogRepository,
                            AuthorizedVehicleRepository authorizedVehicleRepository,
                            ParkingLotRepository parkingLotRepository) {

    public Mono<AccessLog> registerEntry(String plate, UUID parkingLotId, String comments,
            String userType) {
        LocalDateTime now = LocalDateTime.now();
        return parkingLotRepository.findById(parkingLotId)
                .switchIfEmpty(Mono.error(new RuntimeException("Parking lot not found")))
                .flatMap(unused -> authorizedVehicleRepository.findByPlate(plate)
                        .map(vehicle -> {
                            if (Boolean.FALSE.equals(vehicle.getIsActive())) {
                                throw new RuntimeException("Vehicle authorized but inactive");
                            }
                            return vehicle;
                        }))
                .flatMap(vehicle -> createAccessLog(plate, parkingLotId, AccessType.ENTRY, comments,
                        userType))
                .switchIfEmpty(Mono.defer(() -> {
                    return createAccessLog(plate, parkingLotId, AccessType.ENTRY, comments,
                            userType);
                }));
    }

    public Mono<AccessLog> registerExit(String plate, UUID parkingLotId, String comments,
            String userType) {
        return createAccessLog(plate, parkingLotId, AccessType.EXIT, comments, userType);
    }

    public Mono<AccessLog> registerRejection(String plate, UUID parkingLotId, String comments,
            String userType) {
        return createAccessLog(plate, parkingLotId, AccessType.REJECTED, comments, userType);
    }

    private Mono<AccessLog> createAccessLog(String plate, UUID parkingLotId,
            AccessType type, String comments, String userType) {
        return accessLogRepository.create(AccessLog.builder()
                .id(UUID.randomUUID())
                .plate(plate)
                .timestamp(LocalDateTime.now())
                .type(type)
                .parkingLotId(parkingLotId)
                .comments(comments)
                .userType(userType)
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

    public Mono<co.edu.sena.model.vehicle.AuthorizedVehicle> findAuthorizedVehicle(String plate) {
        return authorizedVehicleRepository.findByPlate(plate);
    }
}
