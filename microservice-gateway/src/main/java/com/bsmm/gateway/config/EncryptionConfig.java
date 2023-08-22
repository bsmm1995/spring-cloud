package com.bsmm.gateway.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
public class EncryptionConfig {
    @Value("${encryption.enabled:true}")
    private Boolean isActiveEncrypt;

    @Value("${encryption.keys.private}")
    private String privateKey;

    @Value("${encryption.keys.public}")
    private String publicKey;

    private final KeyPair rsaKeyPair;

    @SneakyThrows
    public EncryptionConfig() {
        KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
        rsaGen.initialize(2048);
        rsaKeyPair = rsaGen.generateKeyPair();
    }

    public RSAPublicKey getPublicKey() {
        return (RSAPublicKey) rsaKeyPair.getPublic();
    }

    public RSAPrivateKey getPrivateKey() {
        return (RSAPrivateKey) rsaKeyPair.getPrivate();
    }
}
