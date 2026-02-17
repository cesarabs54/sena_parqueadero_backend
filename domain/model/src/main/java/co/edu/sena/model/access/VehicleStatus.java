package co.edu.sena.model.access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleStatus {

    private String plate;
    private String currentStatus; // "IN" or "OUT"
    private boolean isAuthorized;
    private String ownerName;
}
