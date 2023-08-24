package com.bsmm.gateway.util;

import lombok.SneakyThrows;

import java.security.KeyFactory;

public class JWEUtil {
    private JWEUtil() {
    }

    public static final String HEADER_KEY_NAME = "key";
    public static final String ALGORITHM_RSA_NAME = "RSA";
    public static final String INSTANCE_RSA_NAME = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
    public static final String SYMMETRIC_ALGORITHM_NAME = "AES";

    @SneakyThrows
    public static KeyFactory geyKeyFactory() {
        return KeyFactory.getInstance(ALGORITHM_RSA_NAME);
    }
}

