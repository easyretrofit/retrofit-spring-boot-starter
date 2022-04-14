package io.github.liuziyuan.retrofit.extension;

import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.URI;

/**
 * @author liuziyuan
 */

@Component
public class RetrofitCloudInterceptor extends BaseInterceptor {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RetrofitResourceContext context;

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        final Method method = super.getRequestMethod(request);
        String clazzName = super.getClazzNameByMethod(method);
        final RetrofitServiceBean currentServiceBean = context.getRetrofitServiceBean(clazzName);
        final String serviceName = currentServiceBean.getRetrofitCloudService().name();
        if (StringUtils.isNotEmpty(serviceName)) {
            final URI uri = loadBalancerClient.choose(serviceName).getUri();
            final HttpUrl httpUrl = HttpUrl.get(uri);
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
