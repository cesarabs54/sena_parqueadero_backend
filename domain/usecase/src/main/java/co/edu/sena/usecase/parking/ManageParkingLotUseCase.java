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
  private final co.edu.sena.usecase.access.AccessUseCase accessUseCase;

  public Mono<ParkingLot> createParkingLot(ParkingLot parkingLot) {
    return parkingLotRepository.create(parkingLot.toBuilder().id(UUID.randomUUID()).build());
  }

  public Mono<ParkingLot> updateParkingLot(UUID id, ParkingLot parkingLot) {
    return parkingLotRepository.findById(id)
            .flatMap(existing -> {
              ParkingLot updated = existing.toBuilder()
                      .name(parkingLot.getName())
                      .capacity(parkingLot.getCapacity())
                      .address(parkingLot.getAddress())
                      .build();
              return parkingLotRepository.update(updated);
            });
  }

  public Flux<ParkingLot> getAllParkingLots() {
    return parkingLotRepository.findAll()
            .flatMap(parkingLot -> accessUseCase.getOccupancy(parkingLot.getId())
                    .map(occupancy -> parkingLot.toBuilder()
                            .occupied(occupancy.intValue())
                            .build())
                    .defaultIfEmpty(parkingLot.toBuilder().occupied(0).build()));
  }

  public Mono<ParkingLot> getParkingLotById(UUID id) {
    return parkingLotRepository.findById(id)
            .flatMap(parkingLot -> accessUseCase.getOccupancy(parkingLot.getId())
                    .map(occupancy -> parkingLot.toBuilder()
                            .occupied(occupancy.intValue())
                            .build())
                    .defaultIfEmpty(parkingLot.toBuilder().occupied(0).build()));
  }
}
