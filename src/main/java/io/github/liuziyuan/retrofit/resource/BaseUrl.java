package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.exception.RetrofitBaseUrlException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * @author liuziyuan
 */

@Getter
public class BaseUrl {
    private final String realBaseUrl;
    private final String realHostUrl;
    private final String realPrefixUrl;
    private static final String BASE_URL_NULL = "Retrofit baseUrl is null";

    public BaseUrl(){
        this.realBaseUrl = StringUtils.EMPTY;
        this.realHostUrl = StringUtils.EMPTY;
        this.realPrefixUrl = StringUtils.EMPTY;
    }

    public BaseUrl(String realBaseUrl) {
        this.realBaseUrl = realBaseUrl;
        this.realHostUrl = getRealHostUrl(realBaseUrl);
        this.realPrefixUrl = getRealPrefixUrl(realBaseUrl);
    }

    @SneakyThrows
    private String getRealHostUrl(String realBaseUrl) {
        if (realBaseUrl == null) {
            throw new RetrofitBaseUrlException(BASE_URL_NULL);
        }
        URL url = new URL(realBaseUrl);
        String path = url.getPath();
        return "/".equals(path) ? realBaseUrl : StringUtils.replace(realBaseUrl, path, "/");
    }

    @SneakyThrows
    private String getRealPrefixUrl(String realBaseUrl) {
        if (realBaseUrl == null) {
            throw new RetrofitBaseUrlException(BASE_URL_NULL);
        }
        URL url = new URL(realBaseUrl);
        return "/".equals(url.getPath()) ? "" : url.getPath();
    }
}
