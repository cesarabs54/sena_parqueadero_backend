package co.edu.sena.r2dbc.parking;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ParkingLotEntityRepository extends ReactiveCrudRepository<ParkingLotEntity, UUID> {

}
