package co.edu.sena.model.vehicle.gateways;

import co.edu.sena.model.vehicle.AuthorizedVehicle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorizedVehicleRepository {

    Mono<AuthorizedVehicle> findByPlate(String plate);

    Mono<AuthorizedVehicle> save(AuthorizedVehicle vehicle);

  Flux<AuthorizedVehicle> findAll();

  Mono<Void> deleteByPlate(String plate);
}
