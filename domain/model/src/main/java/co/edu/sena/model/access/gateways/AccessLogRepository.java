package co.edu.sena.model.access.gateways;

import co.edu.sena.model.access.AccessLog;
import reactor.core.publisher.Mono;

public interface AccessLogRepository {

  Mono<AccessLog> save(AccessLog accessLog);

  reactor.core.publisher.Flux<AccessLog> findAll();

  Mono<Long> countByParkingLotIdAndType(java.util.UUID parkingLotId, AccessLog.AccessType type);
}
