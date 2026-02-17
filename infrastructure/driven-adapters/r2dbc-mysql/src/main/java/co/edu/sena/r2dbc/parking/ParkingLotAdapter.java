package co.edu.sena.r2dbc.parking;

import co.edu.sena.model.parking.ParkingLot;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ParkingLotAdapter implements ParkingLotRepository {

    private final ParkingLotEntityRepository repository;

    @Override
    public Mono<ParkingLot> findById(UUID id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<ParkingLot> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<ParkingLot> create(ParkingLot parkingLot) {
        return repository.save(toEntity(parkingLot).toBuilder().newRecord(true).build())
                .map(this::toDomain);
    }

    @Override
    public Mono<ParkingLot> update(ParkingLot parkingLot) {
        return repository.save(toEntity(parkingLot).toBuilder().newRecord(false).build())
                .map(this::toDomain);
    }

    private ParkingLot toDomain(ParkingLotEntity entity) {
        return ParkingLot.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .address(entity.getAddress())
                .occupied(0)
                .build();
    }

    private ParkingLotEntity toEntity(ParkingLot parkingLot) {
        return ParkingLotEntity.builder()
                .id(parkingLot.getId())
                .name(parkingLot.getName())
                .capacity(parkingLot.getCapacity())
                .address(parkingLot.getAddress())
                .build();
    }
}
