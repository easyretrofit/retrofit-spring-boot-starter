package io.github.liuziyuan.retrofit.extension;

import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
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
    private volatile String host;

    public DynamicBaseUrlInterceptor(RetrofitResourceContext context) {
        super(context);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        final Method method = super.getRequestMethod(request);
        String clazzName = super.getClazzNameByMethod(method);
        final RetrofitServiceBean currentServiceBean = super.context.getRetrofitServiceBean(clazzName);
        final String realDynamicHostUrl = currentServiceBean.getRetrofitUrl().getDynamicUrl().getRealHostUrl();
        if (StringUtils.isNotEmpty(realDynamicHostUrl)) {
            HttpUrl newUrl = request.url().newBuilder().host(host).build();
            request = request.newBuilder().url(newUrl).build();
        }
        return chain.proceed(request);
    }
}
