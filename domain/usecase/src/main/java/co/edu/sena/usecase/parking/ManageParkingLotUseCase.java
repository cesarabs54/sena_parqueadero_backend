package co.edu.sena.usecase.parking;

import co.edu.sena.model.parking.ParkingLot;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ManageParkingLotUseCase {

    private final ParkingLotRepository parkingLotRepository;

    public Mono<ParkingLot> createParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    public Mono<ParkingLot> updateParkingLot(UUID id, ParkingLot parkingLot) {
        return parkingLotRepository.findById(id)
                .flatMap(existing -> {
                    ParkingLot updated = existing.toBuilder()
                            .name(parkingLot.getName())
                            .capacity(parkingLot.getCapacity())
                            .address(parkingLot.getAddress())
                            .build();
                    return parkingLotRepository.save(updated);
                });
    }

    public Flux<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAll();
    }

    public Mono<ParkingLot> getParkingLotById(UUID id) {
        return parkingLotRepository.findById(id);
    }
}
