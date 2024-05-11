package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.exception.BaseUrlException;
import io.github.liuziyuan.retrofit.core.util.RetrofitUrlUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * @author liuziyuan
 */
public class BaseUrl {
    private String realBaseUrl;
    private String realHostUrl;
    private String realPrefixUrl;
    private static final String BASE_URL_NULL = "Retrofit base URL is null";

    public BaseUrl() {
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
            throw new BaseUrlException(BASE_URL_NULL);
        }
        URL url = RetrofitUrlUtils.getURL(realBaseUrl);
        String path = url.getPath();
        return "/".equals(path) ? realBaseUrl : StringUtils.replace(realBaseUrl, path, "/");
    }

    @SneakyThrows
    private String getRealPrefixUrl(String realBaseUrl) {
        if (realBaseUrl == null) {
            throw new BaseUrlException(BASE_URL_NULL);
        }
        URL url = RetrofitUrlUtils.getURL(realBaseUrl);
        return "/".equals(url.getPath()) ? "" : url.getPath();
    }

    public String getRealBaseUrl() {
        return realBaseUrl;
    }

    public void setRealBaseUrl(String realBaseUrl) {
        this.realBaseUrl = realBaseUrl;
    }

    public String getRealHostUrl() {
        return realHostUrl;
    }

    public void setRealHostUrl(String realHostUrl) {
        this.realHostUrl = realHostUrl;
    }

    public String getRealPrefixUrl() {
        return realPrefixUrl;
    }

    public void setRealPrefixUrl(String realPrefixUrl) {
        this.realPrefixUrl = realPrefixUrl;
    }
}
