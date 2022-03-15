package io.github.liuziyuan.retrofit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

/**
 * @author liuziyuan
 */
@Slf4j
public class RetrofitUtils {

    private static final String SUFFIX = "/";

    private RetrofitUtils() {
    }

    public static String convertBaseUrl(String baseUrl, Environment environment) {
        try {
            baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
        } catch (IllegalArgumentException exception) {
            log.warn("The URL {} could not be resolved, Retrofit Service will be discarded", baseUrl);
            return null;
        }
        // 解析baseUrl占位符
        if (!baseUrl.endsWith(SUFFIX)) {
            baseUrl += SUFFIX;
        }
        return baseUrl;
    }
}
