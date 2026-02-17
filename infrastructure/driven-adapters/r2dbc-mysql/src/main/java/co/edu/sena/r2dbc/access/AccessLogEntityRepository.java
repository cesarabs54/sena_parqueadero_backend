package co.edu.sena.r2dbc.access;

import co.edu.sena.model.access.AccessLog.AccessType;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AccessLogEntityRepository extends ReactiveCrudRepository<AccessLogEntity, UUID> {

    Mono<Long> countByParkingLotIdAndType(UUID parkingLotId, AccessType type);

    Mono<AccessLogEntity> findFirstByPlateOrderByTimestampDesc(String plate);
}
