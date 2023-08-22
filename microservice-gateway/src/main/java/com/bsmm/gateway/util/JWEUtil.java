package com.bsmm.gateway.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import lombok.SneakyThrows;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JWEUtil {
    private JWEUtil() {
    }

    @SneakyThrows
    public static String decrypt(String cipherText, RSAPrivateKey privateKey) {
        JWEObject jwe = JWEObject.parse(cipherText);
        jwe.decrypt(new RSADecrypter(privateKey));
        return jwe.getPayload().toString();
    }

    @SneakyThrows
    public static String encrypt(String plainText, RSAPublicKey publicKey) {
        JWEAlgorithm alg = JWEAlgorithm.RSA_OAEP_256;
        EncryptionMethod enc = EncryptionMethod.A128CBC_HS256;

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(enc.cekBitLength());
        SecretKey cek = keyGenerator.generateKey();

        JWEObject jwe = new JWEObject(new JWEHeader(alg, enc), new Payload(plainText));
        jwe.encrypt(new RSAEncrypter(publicKey, cek));
        return jwe.serialize();
    }
}

