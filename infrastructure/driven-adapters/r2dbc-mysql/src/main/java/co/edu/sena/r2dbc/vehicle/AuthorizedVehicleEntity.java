package co.edu.sena.r2dbc.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("AUTHORIZED_VEHICLE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizedVehicleEntity {

    @Id
    private String plate;
    @Column("owner_name")
    private String ownerName;
    @Column("is_active")
    private Boolean isActive;
}
