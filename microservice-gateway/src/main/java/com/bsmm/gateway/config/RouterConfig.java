package com.bsmm.gateway.config;

import com.bsmm.gateway.EncryptedMessage;
import com.bsmm.gateway.util.JWEUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RouterConfig {
    private final EncryptionConfig config;

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

    public GatewayFilterSpec setGatewayFilterSpec(GatewayFilterSpec filter) {
        return filter.modifyRequestBody(EncryptedMessage.class, String.class, (exchange, originalRequest) -> {
                    log.info("originalRequest {}", originalRequest);
                    return originalRequest != null ?
                            Mono.just(JWEUtil.decrypt(originalRequest.getData(), config.getPrivateKey())) : Mono.empty();
                })
                .modifyResponseBody(String.class, EncryptedMessage.class, (exchange, originalResponse) -> {
                    log.info("originalResponse {}", originalResponse);
                    return originalResponse != null ?
                            Mono.just(new EncryptedMessage(JWEUtil.encrypt(originalResponse, config.getClientPublicKey()))) : Mono.empty();
                })
                .addRequestHeader("test", "test")
                .addResponseHeader("key", "encrypted-key");
    }
}
