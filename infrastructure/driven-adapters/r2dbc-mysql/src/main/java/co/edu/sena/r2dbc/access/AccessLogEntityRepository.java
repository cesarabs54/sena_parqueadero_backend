package co.edu.sena.r2dbc.access;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccessLogEntityRepository extends ReactiveCrudRepository<AccessLogEntity, UUID> {

    reactor.core.publisher.Mono<Long> countByParkingLotIdAndType(UUID parkingLotId,
            co.edu.sena.model.access.AccessLog.AccessType type);

    reactor.core.publisher.Mono<AccessLogEntity> findFirstByPlateOrderByTimestampDesc(String plate);
}
