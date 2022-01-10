package com.github.liuziyuan.retrofit.model;

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

    private String inputBaseUrl;
    private String realBaseUrl;
    private String realHostUrl;
    private String realPrefixUrl;

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
        if (realBaseUrl != null || realBaseUrl.length() == 0) {
            URL url = new URL(realBaseUrl);
            String path = url.getPath();
            return "/".equals(path) ? realBaseUrl : StringUtils.replace(realBaseUrl, path, "/");
        }
        return null;
    }

    @SneakyThrows
    private String getRealPrefixUrl(String realBaseUrl) {
        if (realBaseUrl != null || realBaseUrl.length() == 0) {
            URL url = new URL(realBaseUrl);
            return "/".equals(url.getPath()) ? "" : url.getPath();
        }
        return null;
    }


}
