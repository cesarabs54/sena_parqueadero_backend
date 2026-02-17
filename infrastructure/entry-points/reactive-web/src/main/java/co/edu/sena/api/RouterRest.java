package co.edu.sena.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/access/entry"), handler::handleEntry)
                .andRoute(POST("/api/exit"), handler::handleExit)
                .andRoute(GET("/api/access/status/{plate}"), handler::handleVehicleStatus)
                .andRoute(GET("/api/access/occupancy/{id}"), handler::handleOccupancy)
                // Vehicle Management
                .andRoute(POST("/api/vehicles"), handler::createVehicle)
                .andRoute(GET("/api/vehicles"), handler::getAllVehicles)
                .andRoute(DELETE("/api/vehicles/{plate}"), handler::deleteVehicle)
                // Admin Panel
                .andRoute(GET("/api/access/logs"), handler::getAccessLogs)
                .andRoute(GET("/api/parking-lot"), handler::getAllParkingLots) // Listing zones
                .andRoute(POST("/api/parking-lot"), handler::createParkingLot) // New zone
                .andRoute(
                        org.springframework.web.reactive.function.server.RequestPredicates.PUT(
                                "/api/parking-lot/{id}"),
                        handler::updateParkingLot);
    }
}
