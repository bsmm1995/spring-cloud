package com.bsmm.gateway.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.util.Base64;

public class EncryptDecryptRSA {
    private EncryptDecryptRSA() {
    }

    @SneakyThrows
    public static String decode(String toDecode, PrivateKey privateKey) {
        Cipher cipher = Cipher.getInstance(JWEUtil.INSTANCE_RSA_NAME);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = Base64.getDecoder().decode(toDecode);
        byte[] bytes = cipher.doFinal(result);
        return new String(bytes);
    }
}
