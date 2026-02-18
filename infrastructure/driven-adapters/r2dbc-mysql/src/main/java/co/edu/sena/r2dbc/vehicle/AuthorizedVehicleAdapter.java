package co.edu.sena.r2dbc.vehicle;

import co.edu.sena.model.vehicle.AuthorizedVehicle;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import java.util.UUID;
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
        return repository.findByPlate(plate)
                .map(this::toDomain);
    }

    @Override
    public Flux<AuthorizedVehicle> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteByPlate(String plate) {
        return repository.deleteByPlate(plate);
    }

    @Override
    public Mono<AuthorizedVehicle> save(AuthorizedVehicle vehicle) {
        AuthorizedVehicleEntity entity = toEntity(vehicle);
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
            entity.setNewRecord(true);
        }
        return repository.save(entity)
                .map(this::toDomain);
    }

    private AuthorizedVehicle toDomain(AuthorizedVehicleEntity entity) {
        return AuthorizedVehicle.builder()
                .id(entity.getId())
                .plate(entity.getPlate())
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .vehicleType(entity.getVehicleType())
                .contractType(entity.getContractType())
                .jobTitle(entity.getJobTitle())
                .email(entity.getEmail())
                .contact(entity.getContact())
                .isActive(entity.getIsActive())
                .build();
    }

    private AuthorizedVehicleEntity toEntity(AuthorizedVehicle vehicle) {
        return AuthorizedVehicleEntity.builder()
                .id(vehicle.getId())
                .plate(vehicle.getPlate())
                .documentType(vehicle.getDocumentType())
                .documentNumber(vehicle.getDocumentNumber())
                .firstName(vehicle.getFirstName())
                .lastName(vehicle.getLastName())
                .vehicleType(vehicle.getVehicleType())
                .contractType(vehicle.getContractType())
                .jobTitle(vehicle.getJobTitle())
                .email(vehicle.getEmail())
                .contact(vehicle.getContact())
                .isActive(vehicle.getIsActive())
                .build();
    }
}
