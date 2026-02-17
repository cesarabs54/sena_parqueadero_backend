package co.edu.sena.model.parking.gateways;

import co.edu.sena.model.parking.ParkingLot;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParkingLotRepository {

    Mono<ParkingLot> findById(UUID id);

    Flux<ParkingLot> findAll();

  Mono<ParkingLot> create(ParkingLot parkingLot);

  Mono<ParkingLot> update(ParkingLot parkingLot);
}
