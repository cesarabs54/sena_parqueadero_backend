package co.edu.sena.api;

import co.edu.sena.usecase.access.AccessUseCase;
import co.edu.sena.usecase.vehicle.ManageVehiclesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final AccessUseCase accessUseCase;
    private final ManageVehiclesUseCase manageVehiclesUseCase;
    private final co.edu.sena.usecase.parking.ManageParkingLotUseCase manageParkingLotUseCase;

    public Mono<ServerResponse> handleEntry(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessRequestDTO.class)
                .flatMap(request -> accessUseCase.registerEntry(request.plate(),
                        request.parkingLotId(), request.comments(), request.userType()))
                .flatMap(log -> ServerResponse.ok().bodyValue(log))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> handleExit(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessRequestDTO.class)
                .flatMap(request -> accessUseCase.registerExit(request.plate(),
                        request.parkingLotId(), request.comments(), request.userType()))
                .flatMap(log -> ServerResponse.ok().bodyValue(log))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> handleRejection(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessRequestDTO.class)
                .flatMap(request -> accessUseCase.registerRejection(request.plate(),
                        request.parkingLotId(), request.comments(), request.userType()))
                .flatMap(log -> ServerResponse.ok().bodyValue(log))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> handleOccupancy(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return accessUseCase.getOccupancy(java.util.UUID.fromString(id))
                .flatMap(occupancy -> ServerResponse.ok().bodyValue(occupancy))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> handleVehicleStatus(ServerRequest serverRequest) {
        String plate = serverRequest.pathVariable("plate");
        return accessUseCase.getVehicleStatus(plate)
                .flatMap(status -> ServerResponse.ok().bodyValue(status))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> getUserByPlate(ServerRequest serverRequest) {
        String plate = serverRequest.pathVariable("plate");
        return accessUseCase.findAuthorizedVehicle(plate)
                .map(this::toUserDTO)
                .flatMap(userDTO -> ServerResponse.ok().bodyValue(userDTO))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    // Vehicle Management
    public Mono<ServerResponse> createVehicle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .map(this::toAuthorizedVehicle)
                .flatMap(manageVehiclesUseCase::createVehicle)
                .flatMap(vehicle -> ServerResponse.ok().bodyValue(toUserDTO(vehicle)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> getAllVehicles(ServerRequest serverRequest) {
        return ServerResponse.ok().body(
                manageVehiclesUseCase.getAllVehicles().map(this::toUserDTO),
                UserDTO.class);
    }

    public Mono<ServerResponse> deleteVehicle(ServerRequest serverRequest) {
        String plate = serverRequest.pathVariable("plate");
        return manageVehiclesUseCase.deleteVehicle(plate)
                .then(ServerResponse.ok().build())
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    // Admin Panel
    public Mono<ServerResponse> getAccessLogs(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(accessUseCase.getAllAccessLogs(), co.edu.sena.model.access.AccessLog.class);
    }

    // Parking Lot Management
    public Mono<ServerResponse> getAllParkingLots(ServerRequest serverRequest) {
        return ServerResponse.ok().body(manageParkingLotUseCase.getAllParkingLots(),
                co.edu.sena.model.parking.ParkingLot.class);
    }

    public Mono<ServerResponse> updateParkingLot(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(co.edu.sena.model.parking.ParkingLot.class)
                .flatMap(parkingLot -> manageParkingLotUseCase.updateParkingLot(
                        java.util.UUID.fromString(id),
                        parkingLot))
                .flatMap(updated -> ServerResponse.ok().bodyValue(updated))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> createParkingLot(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(co.edu.sena.model.parking.ParkingLot.class)
                .flatMap(manageParkingLotUseCase::createParkingLot)
                .flatMap(created -> ServerResponse.ok().bodyValue(created))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    // Mappers
    private co.edu.sena.model.vehicle.AuthorizedVehicle toAuthorizedVehicle(UserDTO dto) {
        return co.edu.sena.model.vehicle.AuthorizedVehicle.builder()
                .id(dto.id())
                .plate(dto.placa())
                .documentType(dto.tipoDocumento())
                .documentNumber(dto.numeroDocumento())
                .firstName(dto.nombres())
                .lastName(dto.apellidos())
                .vehicleType(dto.tipoVehiculo())
                .contractType(dto.tipoContrato())
                .jobTitle(dto.cargo())
                .email(dto.email())
                .contact(dto.contacto())
                .isActive("Activo".equalsIgnoreCase(dto.estado()))
                .build();
    }

    private UserDTO toUserDTO(co.edu.sena.model.vehicle.AuthorizedVehicle vehicle) {
        return new UserDTO(
                vehicle.getId(),
                vehicle.getPlate(),
                vehicle.getVehicleType(),
                vehicle.getDocumentType(),
                vehicle.getDocumentNumber(),
                vehicle.getFirstName(),
                vehicle.getLastName(),
                vehicle.getContractType(),
                vehicle.getJobTitle(),
                vehicle.getEmail(),
                vehicle.getContact(),
                Boolean.TRUE.equals(vehicle.getIsActive()) ? "Activo" : "Inactivo");
    }
}
