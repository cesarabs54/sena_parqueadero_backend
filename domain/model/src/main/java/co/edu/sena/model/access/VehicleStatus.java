package co.edu.sena.model.access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleStatus {

    private String plate;
    private String currentStatus; // "IN" or "OUT"
    private boolean isAuthorized;
    private String ownerName;
}
