package com.github.liuziyuan.retrofit.util;

import org.springframework.core.env.Environment;

/**
 * @author liuziyuan
 */
public class RetrofitUtils {

    private static final String SUFFIX = "/";

    private RetrofitUtils() {
    }

    public static String convertBaseUrl(String baseUrl, Environment environment) {
        baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
        // 解析baseUrl占位符
        if (!baseUrl.endsWith(SUFFIX)) {
            baseUrl += SUFFIX;
        }
        return baseUrl;
    }
}
