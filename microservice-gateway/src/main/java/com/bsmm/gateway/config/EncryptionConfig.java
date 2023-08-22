package com.bsmm.gateway.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class EncryptionConfig {
    private final RSAPrivateKey rsaPrivateKey;
    private final RSAPublicKey rsaClientPublicKey;
    private final String strPublicKey;

    @SneakyThrows
    public EncryptionConfig(@Value("${encryption.keys.private}") String strPrivateKey,
                            @Value("${encryption.keys.public}") String strPublicKey,
                            @Value("${encryption.keys.public-client}") String strClientPublicKey) {
        this.strPublicKey = strPublicKey;
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(strPrivateKey));
        rsaPrivateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(strClientPublicKey));
        rsaClientPublicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
    }

    public RSAPublicKey getClientPublicKey() {
        return rsaClientPublicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return rsaPrivateKey;
    }

    public String getPublicKey() {
        return strPublicKey;
    }
}
