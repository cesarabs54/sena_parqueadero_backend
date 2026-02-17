package co.edu.sena.r2dbc.parking;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("PARKING_LOT")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParkingLotEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private String name;
    private Integer capacity;
    private String address;

    @org.springframework.data.annotation.Transient
    @Builder.Default
    private boolean newRecord = false;

    @Override
    public boolean isNew() {
        return newRecord;
    }
}
