package io.github.liuziyuan.retrofit.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UniqueKeyUtils {

    private static final String ALGORITHM_MD5 = "MD5";

    public static String generateUniqueKey(String... values) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            for (String value : values) {
                md.update(value.getBytes());
            }
            byte[] digest = md.digest();
            for (byte b : digest) {
                sb.append(String.format("%02x", 0xFF & b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}
