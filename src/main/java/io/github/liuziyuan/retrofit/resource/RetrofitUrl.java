package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.util.RetrofitUrlUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * RetrofitUrl object
 *
 * @author liuziyuan
 */
@Getter
public class RetrofitUrl {

    private final Environment environment;
    private final String inputDefaultBaseUrl;
    private final String inputDynamicBaseUrl;
    private BaseUrl defaultUrl;
    private BaseUrl dynamicUrl;
    private String retrofitUrlPrefix;
    private boolean isDynamicUrl;


    public RetrofitUrl(String baseUrl, String inputDynamicBaseUrl, String retrofitUrlPrefix, Environment environment) {
        this.environment = environment;
        this.inputDefaultBaseUrl = baseUrl;
        this.inputDynamicBaseUrl = inputDynamicBaseUrl;
        setRetrofitUrlPrefix(retrofitUrlPrefix);
        setDefaultUrl();
        setDynamicUrl();

    }

    private void setRetrofitUrlPrefix(String url) {
        if (url != null) {
            retrofitUrlPrefix = RetrofitUrlUtils.convertBaseUrl(url, environment, false);
        } else {
            retrofitUrlPrefix = StringUtils.EMPTY;
        }
    }

    private void setDynamicUrl() {
        if (StringUtils.isNotEmpty(inputDynamicBaseUrl)) {
            this.isDynamicUrl = true;
            String url = RetrofitUrlUtils.convertBaseUrl(inputDynamicBaseUrl, environment, true);
            dynamicUrl = new BaseUrl(url);
        } else {
            dynamicUrl = new BaseUrl();
        }
    }

    private void setDefaultUrl() {
        String url = RetrofitUrlUtils.convertBaseUrl(inputDefaultBaseUrl, environment, true);
        defaultUrl = new BaseUrl(url);
    }


}
