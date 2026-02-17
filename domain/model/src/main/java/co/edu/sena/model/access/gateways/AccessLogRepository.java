package co.edu.sena.model.access.gateways;

import co.edu.sena.model.access.AccessLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccessLogRepository {

  Mono<AccessLog> create(AccessLog accessLog);

  Mono<AccessLog> update(AccessLog accessLog);

  Flux<AccessLog> findAll();

  Mono<Long> countByParkingLotIdAndType(java.util.UUID parkingLotId, AccessLog.AccessType type);

  Mono<AccessLog> findLastByPlate(String plate);
}
