package co.edu.sena.usecase.vehicle;

import co.edu.sena.model.vehicle.AuthorizedVehicle;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record ManageVehiclesUseCase(AuthorizedVehicleRepository authorizedVehicleRepository) {

    public Mono<AuthorizedVehicle> createVehicle(AuthorizedVehicle vehicle) {
        return authorizedVehicleRepository.save(vehicle);
    }

    public Flux<AuthorizedVehicle> getAllVehicles() {
        return authorizedVehicleRepository.findAll();
    }

    public Mono<Void> deleteVehicle(String plate) {
        return authorizedVehicleRepository.deleteByPlate(plate);
    }
}
