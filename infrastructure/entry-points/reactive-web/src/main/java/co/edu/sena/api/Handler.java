package co.edu.sena.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final co.edu.sena.usecase.access.AccessUseCase accessUseCase;
    private final co.edu.sena.usecase.vehicle.ManageVehiclesUseCase manageVehiclesUseCase;

    public Mono<ServerResponse> handleEntry(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessRequestDTO.class)
                .flatMap(request -> accessUseCase.registerEntry(request.getPlate(), request.getParkingLotId()))
                .flatMap(log -> ServerResponse.ok().bodyValue(log))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> handleExit(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessRequestDTO.class)
                .flatMap(request -> accessUseCase.registerExit(request.getPlate(), request.getParkingLotId()))
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

    // Vehicle Management
    public Mono<ServerResponse> createVehicle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(co.edu.sena.model.vehicle.AuthorizedVehicle.class)
                .flatMap(manageVehiclesUseCase::createVehicle)
                .flatMap(vehicle -> ServerResponse.ok().bodyValue(vehicle))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> getAllVehicles(ServerRequest serverRequest) {
        return ServerResponse.ok().body(manageVehiclesUseCase.getAllVehicles(),
                co.edu.sena.model.vehicle.AuthorizedVehicle.class);
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

    private final co.edu.sena.usecase.parking.ManageParkingLotUseCase manageParkingLotUseCase;

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
}
