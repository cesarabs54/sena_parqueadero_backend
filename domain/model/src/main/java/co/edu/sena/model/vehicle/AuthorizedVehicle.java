package co.edu.sena.model.vehicle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AuthorizedVehicle {

    private final String plate;
    private final String ownerName;
    private final Boolean isActive;
}
