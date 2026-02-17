package co.edu.sena.r2dbc.vehicle;

import co.edu.sena.model.vehicle.AuthorizedVehicle;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AuthorizedVehicleAdapter implements AuthorizedVehicleRepository {

    private final AuthorizedVehicleEntityRepository repository;

    @Override
    public Mono<AuthorizedVehicle> findByPlate(String plate) {
        return repository.findById(plate)
                .map(this::toDomain);
    }

    @Override
    public Flux<AuthorizedVehicle> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteByPlate(String plate) {
        return repository.deleteById(
                plate); // Changed to deleteById as deleteByPlate is not a standard R2DBC method
    }

    @Override
    public Mono<AuthorizedVehicle> save(AuthorizedVehicle vehicle) {
        return repository.save(toEntity(vehicle))
                .map(this::toDomain);
    }

    private AuthorizedVehicle toDomain(AuthorizedVehicleEntity entity) {
        return AuthorizedVehicle.builder()
                .plate(entity.getPlate())
                .ownerName(entity.getOwnerName())
                .isActive(entity.getIsActive())
                .build();
    }

    private AuthorizedVehicleEntity toEntity(AuthorizedVehicle vehicle) {
        return AuthorizedVehicleEntity.builder()
                .plate(vehicle.getPlate())
                .ownerName(vehicle.getOwnerName())
                .isActive(vehicle.getIsActive())
                .build();
    }
}
