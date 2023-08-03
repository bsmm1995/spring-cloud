package com.bsmm.gateway.util;

import java.util.Base64;

public class Convert {
    private Convert() {
    }

    public static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String decode(String text) {
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        return new String(decodedBytes);
    }
}
