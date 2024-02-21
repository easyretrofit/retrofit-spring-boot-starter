package io.github.liuziyuan.retrofit.core.extension;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author liuziyuan
 */
public class DynamicBaseUrlInterceptor extends BaseInterceptor {

    public DynamicBaseUrlInterceptor(RetrofitResourceContext context) {
        super(context);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        final Method method = super.getRequestMethod(request);
        String clazzName = super.getClazzNameByMethod(method);
        final RetrofitApiServiceBean currentServiceBean = super.context.getRetrofitServiceBean(clazzName);
        final String realDynamicHostUrl = currentServiceBean.getRetrofitUrl().getDynamicUrl().getRealHostUrl();
        if (StringUtils.isNotEmpty(realDynamicHostUrl)) {
            final HttpUrl httpUrl = HttpUrl.get(realDynamicHostUrl);
            HttpUrl newUrl = request.url().newBuilder()
                    .scheme(httpUrl.scheme())
                    .host(httpUrl.host())
                    .port(httpUrl.port())
                    .build();
            request = request.newBuilder().url(newUrl).build();
        }
        return chain.proceed(request);
    }
}
