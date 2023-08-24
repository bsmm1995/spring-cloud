package com.bsmm.gateway.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptDecrypt {

    private EncryptDecrypt() {
    }

    @SneakyThrows
    public static String encryptJwe(String cipherText, String secretKey) {
        byte[] keyString = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyString, JWEUtil.SYMMETRIC_ALGORITHM_NAME);
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
        Payload payload = new Payload(cipherText);
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(new DirectEncrypter(key));
        return jweObject.serialize();
    }

    @SneakyThrows
    public static String decryptJwe(String cipherText, String secretKey) {
        byte[] keyString = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(keyString, JWEUtil.SYMMETRIC_ALGORITHM_NAME);
        JWEObject jweObject = JWEObject.parse(cipherText);
        jweObject.decrypt(new DirectDecrypter(key));
        return jweObject.getPayload().toString();
    }
}
