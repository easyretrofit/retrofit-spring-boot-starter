package com.github.liuziyuan.retrofit.util;

import org.springframework.core.env.Environment;

/**
 * @author liuziyuan
 * @date 12/10/2021 4:11 PM
 */
public class RetrofitUtils {

    private static final String SUFFIX = "/";

    public static String convertBaseUrl(String baseUrl, Environment environment) {
        baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
        // 解析baseUrl占位符
        if (!baseUrl.endsWith(SUFFIX)) {
            baseUrl += SUFFIX;
        }
        return baseUrl;
    }
}
