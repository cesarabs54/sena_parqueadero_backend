package co.edu.sena.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedVehicle {

  private String plate;
  private String ownerName;
  private Boolean isActive;
}
