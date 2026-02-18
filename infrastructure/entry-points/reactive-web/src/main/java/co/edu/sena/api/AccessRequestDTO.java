package co.edu.sena.api;

import java.util.UUID;

public record AccessRequestDTO(String plate, UUID parkingLotId, String comments, String userType) {

}
