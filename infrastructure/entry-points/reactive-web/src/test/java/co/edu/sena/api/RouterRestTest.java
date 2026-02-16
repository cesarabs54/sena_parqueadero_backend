package co.edu.sena.api;

import co.edu.sena.model.access.AccessLog;
import co.edu.sena.usecase.access.AccessUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RouterRest.class, Handler.class })
class RouterRestTest {

        @Autowired
        private RouterFunction<ServerResponse> routerFunction;

        private WebTestClient webTestClient;

        @MockitoBean
        private AccessUseCase accessUseCase;

        @BeforeEach
        void setUp() {
                webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        }

        @Test
        void testRegisterEntry() {
                AccessLog log = AccessLog.builder()
                                .plate("ABC123")
                                .parkingLotId(UUID.randomUUID())
                                .build();

                when(accessUseCase.registerEntry(any(), any()))
                                .thenReturn(Mono.just(log));

                webTestClient.post()
                                .uri("/api/access/entry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .bodyValue(new AccessRequestDTO("ABC123", UUID.randomUUID()))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$.plate").isEqualTo("ABC123");
        }

        @Test
        void testRegisterExit() {
                AccessLog log = AccessLog.builder()
                                .plate("ABC123")
                                .parkingLotId(UUID.randomUUID())
                                .build();

                when(accessUseCase.registerExit(any(), any()))
                                .thenReturn(Mono.just(log));

                webTestClient.post()
                                .uri("/api/access/exit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .bodyValue(new AccessRequestDTO("ABC123", UUID.randomUUID()))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$.plate").isEqualTo("ABC123");
        }
}
