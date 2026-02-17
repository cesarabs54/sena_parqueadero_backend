package co.edu.sena.r2dbc.vehicle;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AuthorizedVehicleEntityRepository extends
        ReactiveCrudRepository<AuthorizedVehicleEntity, String> {

    Mono<Void> deleteByPlate(String plate);
}
