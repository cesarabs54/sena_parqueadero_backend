package co.edu.sena.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final co.edu.sena.usecase.access.AccessUseCase accessUseCase;

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
}
