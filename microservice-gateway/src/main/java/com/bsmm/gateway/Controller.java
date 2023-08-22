package com.bsmm.gateway;

import com.bsmm.gateway.config.EncryptionConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keys")
public class Controller {
    private final EncryptionConfig config;

    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> getAllBooks() {
        return Mono.just(Map.of("key", config.getPublicKey().getEncoded()));
    }
}
