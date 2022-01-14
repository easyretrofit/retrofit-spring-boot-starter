package com.github.liuziyuan.retrofit.resource;

import com.github.liuziyuan.retrofit.exception.RetrofitBaseUrlNullException;
import com.github.liuziyuan.retrofit.util.RetrofitUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.URL;

/**
 * @author liuziyuan
 * @date 1/6/2022 4:20 PM
 */
@Getter
public class RetrofitUrl {

    private final String inputBaseUrl;
    private final String realBaseUrl;
    private final String realHostUrl;
    private final String realPrefixUrl;
    private static final String BASE_URL_NULL = "Retrofit baseUrl is null";

    public RetrofitUrl(String baseUrl, Environment environment) {
        inputBaseUrl = baseUrl;
        realBaseUrl = RetrofitUtils.convertBaseUrl(baseUrl, environment);
        realHostUrl = getRealHostUrl(realBaseUrl);
        realPrefixUrl = getRealPrefixUrl(realBaseUrl);
    }

    public RetrofitUrl(RetrofitUrl source) {
        this.inputBaseUrl = source.getInputBaseUrl();
        this.realPrefixUrl = source.getRealPrefixUrl();
        this.realHostUrl = source.getRealHostUrl();
        this.realBaseUrl = source.getRealBaseUrl();
    }

    @SneakyThrows
    private String getRealHostUrl(String realBaseUrl) {
        if (realBaseUrl == null) {
            throw new RetrofitBaseUrlNullException(BASE_URL_NULL);
        }
        URL url = new URL(realBaseUrl);
        String path = url.getPath();
        return "/".equals(path) ? realBaseUrl : StringUtils.replace(realBaseUrl, path, "/");
    }

    @SneakyThrows
    private String getRealPrefixUrl(String realBaseUrl) {
        if (realBaseUrl == null) {
            throw new RetrofitBaseUrlNullException(BASE_URL_NULL);
        }
        URL url = new URL(realBaseUrl);
        return "/".equals(url.getPath()) ? "" : url.getPath();
    }


}
