package com.bsmm.gateway.config;

import com.bsmm.gateway.EncryptedMessage;
import com.bsmm.gateway.util.EncryptDecryptRSA;
import com.bsmm.gateway.util.EncryptDecrypt;
import com.bsmm.gateway.util.JWEUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RouterConfig {
    private final EncryptionConfig config;

    @Bean
    public RouteLocator routerBuilder(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes().route("user", r -> r.path("/users/**").filters(this::setGatewayFilterSpec).uri("http://localhost:8081")).route("account", r -> r.path("/accounts/**").filters(this::setGatewayFilterSpec).uri("http://localhost:8082")).build();
    }

    @SneakyThrows
    public GatewayFilterSpec setGatewayFilterSpec(GatewayFilterSpec filter) {
        AtomicReference<String> key = new AtomicReference<>("");
        return filter.modifyRequestBody(EncryptedMessage.class, String.class, (exchange, originalRequest) -> {
            log.info("originalRequest {}", originalRequest);
            key.set(exchange.getRequest().getHeaders().getFirst(JWEUtil.HEADER_KEY_NAME));
            log.info("KEY: {}", key);
            String decryptedKey = EncryptDecryptRSA.decode(key.get(), config.getPrivateKey());
            return originalRequest != null ? Mono.just(EncryptDecrypt.decryptJwe(originalRequest.getData(), decryptedKey)) : Mono.empty();
        }).modifyResponseBody(String.class, EncryptedMessage.class, (exchange, originalResponse) -> {
            log.info("originalResponse {}", originalResponse);
            if (key.get() == null || key.get().isBlank()) {
                return Mono.error(new Throwable());
            }
            exchange.getResponse().getHeaders().add(JWEUtil.HEADER_KEY_NAME, key.getPlain());
            String decryptedKey = EncryptDecryptRSA.decode(key.get(), config.getPrivateKey());
            return originalResponse != null ? Mono.just(new EncryptedMessage(EncryptDecrypt.encryptJwe(originalResponse, decryptedKey))) : Mono.empty();
        });
    }
}
