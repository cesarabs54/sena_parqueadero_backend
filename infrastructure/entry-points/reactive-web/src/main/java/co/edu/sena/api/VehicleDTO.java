package co.edu.sena.api;

import lombok.Builder;

@Builder
public record VehicleDTO(String plate, Boolean isActive) {

}
