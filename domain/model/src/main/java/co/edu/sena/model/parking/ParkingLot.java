package co.edu.sena.model.parking;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ParkingLot {

    private final UUID id;
    private final String name;
    private final Integer capacity;
    private final String address;
}
