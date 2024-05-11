package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.util.RetrofitUrlUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * RetrofitUrl object
 *
 * @author liuziyuan
 */
public class RetrofitUrl {

    private String inputDefaultBaseUrl;
    private String inputDynamicBaseUrl;
    private BaseUrl defaultUrl;
    private BaseUrl dynamicUrl;
    private String retrofitUrlPrefix;
    private UrlStatus urlStatus = UrlStatus.NULL;

    public RetrofitUrl() {
    }


    public RetrofitUrl(String baseUrl, String inputDynamicBaseUrl, String retrofitUrlPrefix, Env env) {
        this.inputDefaultBaseUrl = RetrofitUrlUtils.convertBaseUrl(baseUrl, env::resolveRequiredPlaceholders, false);
        this.inputDynamicBaseUrl = RetrofitUrlUtils.convertBaseUrl(inputDynamicBaseUrl, env::resolveRequiredPlaceholders, false);
        setRetrofitUrlPrefix(retrofitUrlPrefix, env::resolveRequiredPlaceholders);
        setDefaultUrl(env::resolveRequiredPlaceholders);
        setDynamicUrl(env::resolveRequiredPlaceholders);
    }

    private void setRetrofitUrlPrefix(String url, Function<String, String> resolveRequiredPlaceholders) {
        if (url != null) {
            retrofitUrlPrefix = RetrofitUrlUtils.convertBaseUrl(url, resolveRequiredPlaceholders, false);
        } else {
            retrofitUrlPrefix = StringUtils.EMPTY;
        }
    }

    private void setDynamicUrl(Function<String, String> resolveRequiredPlaceholders) {
        if (StringUtils.isNotEmpty(inputDynamicBaseUrl)) {
            String url = RetrofitUrlUtils.convertBaseUrl(inputDynamicBaseUrl, resolveRequiredPlaceholders, true);
            dynamicUrl = new BaseUrl(url);
            if (!urlStatus.equals(UrlStatus.NULL) && !urlStatus.equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                urlStatus = UrlStatus.DEFAULT_DYNAMIC_ALL;
            }
        } else {
            dynamicUrl = new BaseUrl();
        }
    }

    private void setDefaultUrl(Function<String, String> resolveRequiredPlaceholders) {
        String url;
        if (StringUtils.isEmpty(inputDefaultBaseUrl) && StringUtils.isNotEmpty(inputDynamicBaseUrl)) {
            url = RetrofitUrlUtils.getLocalURL().toString();
            urlStatus = UrlStatus.DYNAMIC_URL_ONLY;
        } else if (StringUtils.isEmpty(inputDefaultBaseUrl) && StringUtils.isEmpty(inputDynamicBaseUrl)) {
            url = RetrofitUrlUtils.getLocalURL().toString();
            urlStatus = UrlStatus.NULL;
        } else {
            url = RetrofitUrlUtils.convertBaseUrl(inputDefaultBaseUrl, resolveRequiredPlaceholders, true);
            urlStatus = UrlStatus.DEFAULT_URL_ONLY;
        }
        defaultUrl = new BaseUrl(url);
    }

    public String getInputDefaultBaseUrl() {
        return inputDefaultBaseUrl;
    }

    public String getInputDynamicBaseUrl() {
        return inputDynamicBaseUrl;
    }

    public BaseUrl getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(BaseUrl defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public BaseUrl getDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(BaseUrl dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }

    public String getRetrofitUrlPrefix() {
        return retrofitUrlPrefix;
    }

    public void setRetrofitUrlPrefix(String retrofitUrlPrefix) {
        this.retrofitUrlPrefix = retrofitUrlPrefix;
    }

    public UrlStatus getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(UrlStatus urlStatus) {
        this.urlStatus = urlStatus;
    }

    public void setInputDefaultBaseUrl(String inputDefaultBaseUrl) {
        this.inputDefaultBaseUrl = inputDefaultBaseUrl;
    }

    public void setInputDynamicBaseUrl(String inputDynamicBaseUrl) {
        this.inputDynamicBaseUrl = inputDynamicBaseUrl;
    }
}
