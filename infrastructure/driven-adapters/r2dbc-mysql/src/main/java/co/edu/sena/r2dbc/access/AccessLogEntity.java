package co.edu.sena.r2dbc.access;

import co.edu.sena.model.access.AccessLog;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ACCESS_LOG")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccessLogEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private String plate;
    private LocalDateTime timestamp;
    @Column("entry_type")
    private AccessLog.AccessType type;
    @Column("parking_lot_id")
    private UUID parkingLotId;

    @Transient
    @Builder.Default
    private boolean newRecord = false;

    @Override
    public boolean isNew() {
        return newRecord;
    }
}
