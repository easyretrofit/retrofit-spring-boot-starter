package io.github.liuziyuan.retrofit.core.util;

public class BooleanUtil {

    public static boolean transformToBoolean(String str) {
        if ("true".equalsIgnoreCase(str) || "1".equalsIgnoreCase(str)) {
            return true;
        } else if ("false".equalsIgnoreCase(str) || "0".equalsIgnoreCase(str)) {
            return false;
        } else {
            return false;
        }
    }
}
