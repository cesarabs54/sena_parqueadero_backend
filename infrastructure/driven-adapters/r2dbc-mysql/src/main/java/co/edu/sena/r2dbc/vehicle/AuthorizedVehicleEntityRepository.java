package co.edu.sena.r2dbc.vehicle;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AuthorizedVehicleEntityRepository extends
        ReactiveCrudRepository<AuthorizedVehicleEntity, String> {

}
