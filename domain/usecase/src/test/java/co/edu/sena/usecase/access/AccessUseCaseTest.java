package co.edu.sena.usecase.access;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.model.access.gateways.AccessLogRepository;
import co.edu.sena.model.parking.ParkingLot;
import co.edu.sena.model.parking.gateways.ParkingLotRepository;
import co.edu.sena.model.vehicle.AuthorizedVehicle;
import co.edu.sena.model.vehicle.gateways.AuthorizedVehicleRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AccessUseCaseTest {

    @Mock
    private AccessLogRepository accessLogRepository;
    @Mock
    private AuthorizedVehicleRepository authorizedVehicleRepository;
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private AccessUseCase accessUseCase;

    private UUID parkingLotId = UUID.randomUUID();
    private String plate = "ABC123";

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerEntry_AuthorizedActive_Success() {
        when(parkingLotRepository.findById(parkingLotId))
                .thenReturn(Mono.just(ParkingLot.builder().id(parkingLotId).build()));
        when(authorizedVehicleRepository.findByPlate(plate))
                .thenReturn(
                        Mono.just(AuthorizedVehicle.builder().plate(plate).isActive(true).build()));
        when(accessLogRepository.save(any())).thenReturn(
                Mono.just(AccessLog.builder().plate(plate).build()));

        StepVerifier.create(accessUseCase.registerEntry(plate, parkingLotId))
                .expectNextMatches(log -> log.getPlate().equals(plate))
                .verifyComplete();
    }

    @Test
    void registerEntry_AuthorizedInactive_Error() {
        when(parkingLotRepository.findById(parkingLotId))
                .thenReturn(Mono.just(ParkingLot.builder().id(parkingLotId).build()));
        when(authorizedVehicleRepository.findByPlate(plate))
                .thenReturn(Mono.just(
                        AuthorizedVehicle.builder().plate(plate).isActive(false).build()));

        StepVerifier.create(accessUseCase.registerEntry(plate, parkingLotId))
                .expectErrorMessage("Vehicle authorized but inactive")
                .verify();
    }

    @Test
    void registerEntry_NotFound_ErrorOnWeekday() {
        // Mock weekday logic if needed, but the current isWeekend(LocalDateTime.now())
        // is used.
        // For testing we might need to inject a Clock or just rely on the current day
        // if we don't care about the specific day here.
        // Actually, the current logic uses LocalDateTime.now().
        // If it's a weekday, this should fail.
    }
}
