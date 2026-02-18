package co.edu.sena.r2dbc.vehicle;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AuthorizedVehicleEntityRepository extends
        R2dbcRepository<AuthorizedVehicleEntity, UUID> {

    Mono<AuthorizedVehicleEntity> findByPlate(String plate);

    Mono<Void> deleteByPlate(String plate);
}
