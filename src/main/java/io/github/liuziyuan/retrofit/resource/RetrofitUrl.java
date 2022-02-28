package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.exception.RetrofitBaseUrlNullException;
import io.github.liuziyuan.retrofit.util.RetrofitUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.URL;

/**
 * RetrofitUrl object
 *
 * @author liuziyuan
 */
@Getter
public class RetrofitUrl {

    private final String inputBaseUrl;
    private final String realBaseUrl;
    private final String realHostUrl;
    private final String realPrefixUrl;
    private String retrofitUrlPrefix;
    private static final String BASE_URL_NULL = "Retrofit baseUrl is null";
    private Environment environment;

    public RetrofitUrl(String baseUrl, Environment environment) {
        this.environment = environment;
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


    public void setRetrofitUrlPrefix(String retrofitUrlPrefix) {
        if (retrofitUrlPrefix != null) {
            this.retrofitUrlPrefix = RetrofitUtils.convertBaseUrl(retrofitUrlPrefix, environment);
        } else {
            this.retrofitUrlPrefix = org.apache.commons.lang3.StringUtils.EMPTY;
        }
    }


}
