package com.bsmm.gateway.config;

import com.bsmm.gateway.util.JWEUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class EncryptionConfig {
    private final PrivateKey rsaPrivateKey;
    private final String strPublicKey;

    @SneakyThrows
    public EncryptionConfig(@Value("${encryption.keys.private}") String strPrivateKey,
                            @Value("${encryption.keys.public}") String strPublicKey) {
        this.strPublicKey = strPublicKey;
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(strPrivateKey));
        rsaPrivateKey = JWEUtil.geyKeyFactory().generatePrivate(keySpecPKCS8);
    }


    public PrivateKey getPrivateKey() {
        return rsaPrivateKey;
    }

    public String getPublicKey() {
        return strPublicKey;
    }
}
