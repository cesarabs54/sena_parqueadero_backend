package co.edu.sena.usecase.access;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.model.access.gateways.AccessLogRepository;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AccessUseCase {

    private final AccessLogRepository accessLogRepository;
    private final AuthorizedVehicleRepository authorizedVehicleRepository;
    private final ParkingLotRepository parkingLotRepository;

    public Mono<AccessLog> registerEntry(String plate, UUID parkingLotId) {
        LocalDateTime now = LocalDateTime.now();
        return parkingLotRepository.findById(parkingLotId)
                .switchIfEmpty(Mono.error(new RuntimeException("Parking lot not found")))
                .flatMap(unused -> authorizedVehicleRepository.findByPlate(plate))
                .flatMap(vehicle -> {
                    if (Boolean.FALSE.equals(vehicle.getIsActive())) {
                        return Mono.error(new RuntimeException("Vehicle authorized but inactive"));
                    }
                    return createAccessLog(plate, parkingLotId, AccessLog.AccessType.ENTRY);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (isWeekend(now)) {
                        // Allow non-authorized vehicles on weekends (example rule)
                        return createAccessLog(plate, parkingLotId, AccessLog.AccessType.ENTRY);
                    } else {
                        return Mono.error(
                                new RuntimeException("Vehicle not authorized for weekday access"));
                    }
                }));
    }

    private boolean isWeekend(LocalDateTime dateTime) {
        var day = dateTime.getDayOfWeek();
        return day == java.time.DayOfWeek.SATURDAY || day == java.time.DayOfWeek.SUNDAY;
    }

    public Mono<AccessLog> registerExit(String plate, UUID parkingLotId) {
        return createAccessLog(plate, parkingLotId, AccessLog.AccessType.EXIT);
    }

    private Mono<AccessLog> createAccessLog(String plate, UUID parkingLotId,
            AccessLog.AccessType type) {
        return accessLogRepository.save(AccessLog.builder()
                .id(UUID.randomUUID())
                .plate(plate)
                .timestamp(LocalDateTime.now())
                .type(type)
                .parkingLotId(parkingLotId)
                .build());
    }

  public Mono<Long> getOccupancy(UUID parkingLotId) {
    return Mono.zip(
                    accessLogRepository.countByParkingLotIdAndType(parkingLotId,
                            AccessLog.AccessType.ENTRY),
                    accessLogRepository.countByParkingLotIdAndType(parkingLotId, AccessLog.AccessType.EXIT))
            .map(tuple -> tuple.getT1() - tuple.getT2());
  }
}
