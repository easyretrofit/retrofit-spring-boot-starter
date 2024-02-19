package io.github.liuziyuan.retrofit.util;

import io.github.liuziyuan.retrofit.exception.BaseUrlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author liuziyuan
 */
@Slf4j
public class RetrofitUrlUtils {

    private static final String SUFFIX = "/";

    private RetrofitUrlUtils() {
    }


    public static String convertBaseUrl(String baseUrl, Function<String, String> resolveRequiredPlaceholders, boolean checkUrl) {
        String currentUrl = baseUrl;
        String toLowerUrl = baseUrl;
        try {
            currentUrl = resolveRequiredPlaceholders.apply(currentUrl);
            baseUrl = currentUrl;
        } catch (IllegalArgumentException exception) {
            currentUrl = null;
        }
        if (currentUrl == null) {
            try {
                toLowerUrl = upperToLower(toLowerUrl);
                toLowerUrl = resolveRequiredPlaceholders.apply(toLowerUrl);
                baseUrl = toLowerUrl;
            } catch (IllegalArgumentException exception) {
                toLowerUrl = null;
                log.warn("The URL: {} could not be resolved, Retrofit Service will be discarded", baseUrl);
            }
        }
        if (StringUtils.isNotEmpty(currentUrl) || StringUtils.isNotEmpty(toLowerUrl)) {
            // 解析baseUrl占位符
            if (!baseUrl.endsWith(SUFFIX)) {
                baseUrl += SUFFIX;
            }
            return checkUrl ? getURL(baseUrl).toString() : baseUrl;
        }
        return checkUrl ? getURL(baseUrl).toString() : baseUrl;
    }

    private static String upperToLower(String str) {
        if (StringUtils.isNotEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            //排除第一个字符
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append("-").append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }

            }
            return sb.toString();
        }
        return str;
    }

    public static URL getURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException exception) {
            String url = StringUtils.isEmpty(urlString) ? "EMPTY" : urlString;
            throw new BaseUrlException("URL: [" + url + "] could not be resolved", exception);
        }
    }

    public static URL getLocalURL() {
        try {
            return new URL("http", "localhost", "/");
        } catch (MalformedURLException e) {
            throw new BaseUrlException("Failed to generate localhost URL");
        }
    }
}
