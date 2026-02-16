package co.edu.sena.r2dbc.access;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccessLogEntityRepository extends ReactiveCrudRepository<AccessLogEntity, UUID> {

}
