package com.bsmm.gateway;

import com.bsmm.gateway.dto.EncryptedMessage;
import com.bsmm.gateway.util.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routerBuilder(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("user", r -> r.path("/users/**")
                        .filters(f -> f.modifyRequestBody(EncryptedMessage.class, String.class, (exchange, originalRequest) -> {
                                    log.info("originalRequest {}", originalRequest);
                                    return Mono.just(Convert.decode(originalRequest.getPayload()));
                                })
                                .modifyResponseBody(String.class, EncryptedMessage.class, (exchange, originalResponse) -> {
                                    log.info("originalResponse {}", originalResponse);
                                    return Mono.just(new EncryptedMessage(Convert.encode(originalResponse)));
                                }))
                        .uri("http://localhost:8081"))
                .route("account", r -> r.path("/accounts/**")
                        .uri("http://localhost:8082")).build();
    }

    @Bean
    WebClient client() {
        return WebClient.builder().build();
    }
}
