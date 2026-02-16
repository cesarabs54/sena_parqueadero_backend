package co.edu.sena.api.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.edu.sena.api.Handler;
import co.edu.sena.api.RouterRest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

class ConfigTest {

    private WebTestClient webTestClient;
    private Handler handler;

    @BeforeEach
    void setUp() {
        handler = mock(Handler.class);

        // Mock handler responses
        when(handler.handleEntry(any())).thenReturn(
                ServerResponse.ok().bodyValue("Entry registered")
        );
        when(handler.handleExit(any())).thenReturn(
                ServerResponse.ok().bodyValue("Exit registered")
        );

        RouterRest routerRest = new RouterRest();
        CorsConfig corsConfig = new CorsConfig();
        SecurityHeadersConfig securityHeadersConfig = new SecurityHeadersConfig();

        webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerFunction(handler))
                .webFilter(corsConfig.corsWebFilter(List.of("http://localhost:3000")))
                .webFilter(securityHeadersConfig)
                .build();
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri("/api/access/entry")
                .header("Content-Type", "application/json")
                .bodyValue("{\"plate\":\"ABC123\",\"parkingLotId\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security",
                        "max-age=31536000; includeSubDomains; preload")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}
