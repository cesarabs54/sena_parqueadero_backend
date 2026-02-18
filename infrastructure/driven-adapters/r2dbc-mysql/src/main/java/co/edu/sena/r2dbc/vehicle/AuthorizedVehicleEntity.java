package co.edu.sena.r2dbc.vehicle;

import java.util.UUID;
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
public class AuthorizedVehicleEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    private String plate;

    @Column("document_type")
    private String documentType;

    @Column("document_number")
    private String documentNumber;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("vehicle_type")
    private String vehicleType;

    @Column("contract_type")
    private String contractType;

    @Column("job_title")
    private String jobTitle;

    private String email;

    private String contact;

    @Column("is_active")
    private Boolean isActive;

    @Transient
    @Builder.Default
    private boolean newRecord = false;

    @Override
    public @Nullable UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return newRecord || id == null;
    }
}
