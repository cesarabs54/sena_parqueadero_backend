package co.edu.sena.model.parking;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {

  private UUID id;
  private String name;
  private Integer capacity;
  private String address;
  private Integer occupied;
}
