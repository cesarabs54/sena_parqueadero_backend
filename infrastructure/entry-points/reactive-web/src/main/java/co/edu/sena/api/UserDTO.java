package co.edu.sena.api;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String placa,
        String tipoVehiculo,
        String tipoDocumento,
        String numeroDocumento,
        String nombres,
        String apellidos,
        String tipoContrato,
        String cargo,
        String email,
        String contacto,
        String estado) {

}
