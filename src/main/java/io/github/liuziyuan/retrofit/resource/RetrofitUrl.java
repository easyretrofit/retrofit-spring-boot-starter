package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.util.RetrofitUrlUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * RetrofitUrl object
 *
 * @author liuziyuan
 */
@Slf4j
@Getter
public class RetrofitUrl {

    private final Environment environment;
    private final String inputDefaultBaseUrl;
    private final String inputDynamicBaseUrl;
    private BaseUrl defaultUrl;
    private BaseUrl dynamicUrl;
    private String retrofitUrlPrefix;
    private UrlStatus urlStatus = UrlStatus.NULL;


    public RetrofitUrl(String baseUrl, String inputDynamicBaseUrl, String retrofitUrlPrefix, Environment environment) {
        this.environment = environment;
        this.inputDefaultBaseUrl = RetrofitUrlUtils.convertBaseUrl(baseUrl, environment, false);
        this.inputDynamicBaseUrl = RetrofitUrlUtils.convertBaseUrl(inputDynamicBaseUrl, environment, false);
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
            String url = RetrofitUrlUtils.convertBaseUrl(inputDynamicBaseUrl, environment, true);
            dynamicUrl = new BaseUrl(url);
            if (!urlStatus.equals(UrlStatus.NULL) && !urlStatus.equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                urlStatus = UrlStatus.DEFAULT_DYNAMIC_ALL;
            }
        } else {
            dynamicUrl = new BaseUrl();
        }
    }

    private void setDefaultUrl() {
        String url;
        if (StringUtils.isEmpty(inputDefaultBaseUrl) && StringUtils.isNotEmpty(inputDynamicBaseUrl)) {
            url = RetrofitUrlUtils.getLocalURL(environment).toString();
            urlStatus = UrlStatus.DYNAMIC_URL_ONLY;
        } else if (StringUtils.isEmpty(inputDefaultBaseUrl) && StringUtils.isEmpty(inputDynamicBaseUrl)) {
            url = RetrofitUrlUtils.getLocalURL(environment).toString();
            urlStatus = UrlStatus.NULL;
        } else{
            url = RetrofitUrlUtils.convertBaseUrl(inputDefaultBaseUrl, environment, true);
            urlStatus = UrlStatus.DEFAULT_URL_ONLY;
        }
        defaultUrl = new BaseUrl(url);
    }

}
