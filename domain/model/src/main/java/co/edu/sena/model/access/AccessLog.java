package co.edu.sena.model.access;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AccessLog {

    private final UUID id;
    private final String plate;
    private final LocalDateTime timestamp;
    private final AccessType type;
    private final UUID parkingLotId;
    private final String comments;
    private final String userType; // AUTHORIZED, VISITOR

    public enum AccessType {
        ENTRY, EXIT, REJECTED
    }
}
