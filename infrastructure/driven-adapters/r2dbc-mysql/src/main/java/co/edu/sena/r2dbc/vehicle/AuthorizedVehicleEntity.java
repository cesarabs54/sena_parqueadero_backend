package co.edu.sena.r2dbc.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("AUTHORIZED_VEHICLE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizedVehicleEntity implements Persistable<String> {

    @Id
    private String plate;
    @Column("owner_name")
    private String ownerName;
    @Column("is_active")
    private Boolean isActive;


    @Transient
    @Builder.Default
    private boolean newRecord = false;

    @Override
    public @Nullable String getId() {
        return plate;
    }

    @Override
    public boolean isNew() {
        return newRecord;
    }
}
