package io.github.liuziyuan.retrofit.util;

import io.github.liuziyuan.retrofit.exception.RetrofitBaseUrlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author liuziyuan
 */
@Slf4j
public class RetrofitUtils {

    private static final String SUFFIX = "/";

    private RetrofitUtils() {
    }

    public static String convertBaseUrl(String baseUrl, Environment environment, boolean checkUrl) {
        String currentUrl = baseUrl;
        String toLowerUrl = baseUrl;
        try {
            currentUrl = environment.resolveRequiredPlaceholders(currentUrl);
            baseUrl = currentUrl;
        } catch (IllegalArgumentException exception) {
            currentUrl = null;
        }
        if (currentUrl == null) {
            try {
                toLowerUrl = upperToLower(toLowerUrl);
                toLowerUrl = environment.resolveRequiredPlaceholders(toLowerUrl);
                baseUrl = toLowerUrl;
            } catch (IllegalArgumentException exception) {
                toLowerUrl = null;
                log.warn("The URL {} could not be resolved, Retrofit Service will be discarded", baseUrl);
            }
        }
        if (currentUrl != null || toLowerUrl != null) {
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
            StringBuffer sb = new StringBuffer();
            //排除第一个字符
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append("-" + Character.toLowerCase(c));
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
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException exception) {
            throw new RetrofitBaseUrlException("URL[" + urlString + "] could not be resolved", exception);
        }
    }

}
