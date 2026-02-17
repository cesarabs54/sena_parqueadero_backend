package co.edu.sena.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedVehicle {

  private String plate;
  private String ownerName;
  private Boolean isActive;
}
