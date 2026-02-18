package co.edu.sena.model.vehicle;

import java.util.UUID;
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

    private UUID id;
  private String plate;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String vehicleType;
    private String contractType;
    private String jobTitle;
    private String email;
    private String contact;
  private Boolean isActive;

    public String getOwnerName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}
