package com.bsmm.gateway;

import com.bsmm.gateway.dto.EncryptedMessage;
import com.bsmm.gateway.util.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
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
                        .filters(this::setGatewayFilterSpec)
                        .uri("http://localhost:8081"))
                .route("account", r -> r.path("/accounts/**")
                        .filters(this::setGatewayFilterSpec)
                        .uri("http://localhost:8082")).build();
    }

    public GatewayFilterSpec setGatewayFilterSpec(GatewayFilterSpec f) {
        return f.modifyRequestBody(EncryptedMessage.class, String.class, (exchange, originalRequest) -> {
                    log.info("originalRequest {}", originalRequest);
                    return originalRequest != null ?
                            Mono.just(Convert.decode(originalRequest.getPayload())) : Mono.empty();
                })
                .modifyResponseBody(String.class, EncryptedMessage.class, (exchange, originalResponse) -> {
                    log.info("originalResponse {}", originalResponse);
                    return originalResponse != null ?
                            Mono.just(new EncryptedMessage(Convert.encode(originalResponse))) : Mono.empty();
                })
                .addRequestHeader("test", "test")
                .addResponseHeader("key", "encrypted-key");
    }
}
