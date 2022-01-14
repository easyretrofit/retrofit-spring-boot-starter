package com.github.liuziyuan.retrofit.extension;

import com.github.liuziyuan.retrofit.RetrofitResourceContext;
import com.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Invocation;

import java.util.*;

/**
 * @author liuziyuan
 * @date 1/14/2022 3:24 PM
 */
public class UrlOverWriteInterceptor extends BaseInterceptor {

    public UrlOverWriteInterceptor(RetrofitResourceContext context) {
        super(context);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        String clazzName = Objects.requireNonNull(request.tag(Invocation.class)).method().getDeclaringClass().getName();
        final RetrofitServiceBean currentServiceBean = super.context.getRetrofitServiceBean(clazzName);
        LinkedList<String> pathSegments = new LinkedList<>(request.url().pathSegments());
        String prefix = currentServiceBean.getRetrofitUrl().getRealPrefixUrl();
        List<String> pathPrefixes = Arrays.asList(StringUtils.split(prefix, "/"));
        HttpUrl.Builder builder = request.url().newBuilder();
        pathSegments.forEach(path -> builder.removePathSegment(0));
        pathPrefixes.forEach(builder::addPathSegment);
        pathSegments.forEach(builder::addPathSegment);
        return chain.proceed(request.newBuilder().url(builder.build()).build());
    }
}
