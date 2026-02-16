package co.edu.sena.r2dbc.access;

import co.edu.sena.model.access.AccessLog;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ACCESS_LOG")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLogEntity {

    @Id
    private UUID id;
    private String plate;
    private LocalDateTime timestamp;
    @Column("entry_type")
    private AccessLog.AccessType type;
    @Column("parking_lot_id")
    private UUID parkingLotId;
}
