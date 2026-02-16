package co.edu.sena.r2dbc.parking;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("PARKING_LOT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotEntity {

    @Id
    private UUID id;
    private String name;
    private Integer capacity;
    private String address;
}
