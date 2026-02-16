package co.edu.sena.r2dbc.access;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.model.access.gateways.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccessLogAdapter implements AccessLogRepository {

    private final AccessLogEntityRepository repository;

    @Override
    public Mono<AccessLog> save(AccessLog accessLog) {
        return repository.save(toEntity(accessLog))
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
}
