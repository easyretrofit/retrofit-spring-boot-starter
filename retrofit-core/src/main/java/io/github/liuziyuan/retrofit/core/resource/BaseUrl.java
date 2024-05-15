package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.exception.BaseUrlException;
import io.github.liuziyuan.retrofit.core.util.RetrofitUrlUtils;
import io.github.liuziyuan.retrofit.core.util.UniqueKeyUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * @author liuziyuan
 */
public final class BaseUrl implements UniqueKey {
    private final String realBaseUrl;
    private final String realHostUrl;
    private final String realPrefixUrl;
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

    private String getRealHostUrl(String realBaseUrl) {
        if (realBaseUrl == null) {
            throw new BaseUrlException(BASE_URL_NULL);
        }
        URL url = RetrofitUrlUtils.getURL(realBaseUrl);
        String path = url.getPath();
        return "/".equals(path) ? realBaseUrl : StringUtils.replace(realBaseUrl, path, "/");
    }

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

    public String getRealHostUrl() {
        return realHostUrl;
    }

    public String getRealPrefixUrl() {
        return realPrefixUrl;
    }

    @Override
    public String toString() {
        return "BaseUrl{" +
                "realBaseUrl='" + realBaseUrl + '\'' +
                ", realHostUrl='" + realHostUrl + '\'' +
                ", realPrefixUrl='" + realPrefixUrl + '\'' +
                '}';
    }

    @Override
    public String generateUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }
}
