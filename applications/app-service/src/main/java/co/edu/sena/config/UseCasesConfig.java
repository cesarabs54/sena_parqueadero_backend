package co.edu.sena.config;

import co.edu.sena.model.access.gateways.AccessLogRepository;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import co.edu.sena.usecase.access.AccessUseCase;
import co.edu.sena.usecase.vehicle.ManageVehiclesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.edu.sena.usecase", includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
}, useDefaultFilters = false)
public class UseCasesConfig {
        @Bean
        public AccessUseCase accessUseCase(AccessLogRepository accessLogRepository,
                        AuthorizedVehicleRepository authorizedVehicleRepository,
                        ParkingLotRepository parkingLotRepository) {
                return new AccessUseCase(accessLogRepository, authorizedVehicleRepository, parkingLotRepository);
        }

        @Bean
        public ManageVehiclesUseCase manageVehiclesUseCase(
                AuthorizedVehicleRepository authorizedVehicleRepository) {
                return new ManageVehiclesUseCase(authorizedVehicleRepository);
        }
}
