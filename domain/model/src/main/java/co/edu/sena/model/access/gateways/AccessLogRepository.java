package co.edu.sena.model.access.gateways;

import co.edu.sena.model.access.AccessLog;
import reactor.core.publisher.Mono;

public interface AccessLogRepository {

    Mono<AccessLog> save(AccessLog accessLog);
}
