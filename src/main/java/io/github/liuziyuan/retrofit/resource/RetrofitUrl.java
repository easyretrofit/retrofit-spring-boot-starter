package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.exception.RetrofitBaseUrlException;
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

    private Environment environment;
    private String inputBaseUrl;
    private String inputRetrofitDynamicBaseUrl;
    private BaseUrl defaultUrl;
    private BaseUrl dynamicUrl;
    private String retrofitUrlPrefix;
    private boolean isDynamicUrl;


    public RetrofitUrl(String baseUrl, String inputRetrofitDynamicBaseUrl, String retrofitUrlPrefix, Environment environment) {
        try {
            this.environment = environment;
            this.inputBaseUrl = baseUrl;
            this.inputRetrofitDynamicBaseUrl = inputRetrofitDynamicBaseUrl;
            this.retrofitUrlPrefix = getUrlByConvertBaseUrl(retrofitUrlPrefix, environment);
            String url;
            if (StringUtils.isNotEmpty(inputRetrofitDynamicBaseUrl)) {
                this.isDynamicUrl = true;
                url = RetrofitUtils.convertBaseUrl(inputRetrofitDynamicBaseUrl, environment);
                dynamicUrl = new BaseUrl(url);
            } else {
                dynamicUrl = new BaseUrl();
            }
            url = RetrofitUtils.convertBaseUrl(baseUrl, environment);
            defaultUrl = new BaseUrl(url);
        } catch (RetrofitBaseUrlException exception) {
            throw exception;
        }

    }

    private String getUrlByConvertBaseUrl(String url, Environment environment) {
        if (url != null) {
            return RetrofitUtils.convertBaseUrl(url, environment);
        } else {
            return StringUtils.EMPTY;
        }
    }


}
