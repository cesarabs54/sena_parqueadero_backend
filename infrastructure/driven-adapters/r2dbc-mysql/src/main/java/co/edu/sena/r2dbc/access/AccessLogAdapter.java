package co.edu.sena.r2dbc.access;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.model.access.gateways.AccessLogRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccessLogAdapter implements AccessLogRepository {

    private final AccessLogEntityRepository repository;

    @Override
    public Mono<AccessLog> create(AccessLog accessLog) {
        return repository.save(toEntity(accessLog).toBuilder().newRecord(true).build())
                .map(this::toDomain);
    }

    @Override
    public Mono<AccessLog> update(AccessLog accessLog) {
        return repository.save(toEntity(accessLog).toBuilder().newRecord(false).build())
                .map(this::toDomain);
    }

    @Override
    public Flux<AccessLog> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    private AccessLog toDomain(AccessLogEntity entity) {
        return AccessLog.builder()
                .id(entity.getId())
                .plate(entity.getPlate())
                .timestamp(entity.getTimestamp())
                .type(entity.getType())
                .parkingLotId(entity.getParkingLotId())
                .build();
    }

    private AccessLogEntity toEntity(AccessLog accessLog) {
        return AccessLogEntity.builder()
                .id(accessLog.getId())
                .plate(accessLog.getPlate())
                .timestamp(accessLog.getTimestamp())
                .type(accessLog.getType())
                .parkingLotId(accessLog.getParkingLotId())
                .build();
    }

    @Override
    public Mono<Long> countByParkingLotIdAndType(UUID parkingLotId,
            AccessLog.AccessType type) {
        return repository.countByParkingLotIdAndType(parkingLotId, type);
    }

    @Override
    public Mono<AccessLog> findLastByPlate(String plate) {
        return repository.findFirstByPlateOrderByTimestampDesc(plate)
                .map(this::toDomain);
    }
}
