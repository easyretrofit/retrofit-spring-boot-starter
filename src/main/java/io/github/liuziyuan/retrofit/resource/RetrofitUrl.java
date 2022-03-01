package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.util.RetrofitUtils;
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
    private final String inputBaseUrl;
    private final String inputRetrofitDynamicBaseUrl;
    private BaseUrl defaultUrl;
    private BaseUrl dynamicUrl;
    private final String retrofitUrlPrefix;
    private boolean isDynamicUrl;


    public RetrofitUrl(String baseUrl, String inputRetrofitDynamicBaseUrl, String retrofitUrlPrefix, Environment environment) {
        this.environment = environment;
        this.inputBaseUrl = baseUrl;
        this.inputRetrofitDynamicBaseUrl = inputRetrofitDynamicBaseUrl;
        this.retrofitUrlPrefix = getUrlByConvertBaseUrl(retrofitUrlPrefix, environment);
        String url;
        if (StringUtils.isNotEmpty(inputRetrofitDynamicBaseUrl)) {
            this.isDynamicUrl = true;
            url = RetrofitUtils.convertBaseUrl(inputRetrofitDynamicBaseUrl, environment);
            dynamicUrl = new BaseUrl(url);
        }
        url = RetrofitUtils.convertBaseUrl(baseUrl, environment);
        defaultUrl = new BaseUrl(url);
    }

    private String getUrlByConvertBaseUrl(String url, Environment environment) {
        if (url != null) {
            return RetrofitUtils.convertBaseUrl(url, environment);
        } else {
            return StringUtils.EMPTY;
        }
    }


}
