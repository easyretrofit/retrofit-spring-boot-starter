package io.github.liuziyuan.retrofit.extension;

import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Invocation;
import retrofit2.http.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Create a retrofit object with the same HostURL. Therefore, the interceptor will overwrite the URL every time it requests to fill in the prefix part of the URL
 *
 * @author liuziyuan
 */
public class UrlOverWriteInterceptor extends BaseInterceptor {

    private static final String SLASH = "/";

    public UrlOverWriteInterceptor(RetrofitResourceContext context) {
        super(context);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        final Method method = Objects.requireNonNull(request.tag(Invocation.class)).method();
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            RetrofitHttpAnnotationEnum retrofitHttpAnnotationEnum;
            retrofitHttpAnnotationEnum = RetrofitHttpAnnotationEnum.valueOf("HTTP_" + annotation.annotationType().getSimpleName());
            final String value = getHttpAnnotationValue(method, retrofitHttpAnnotationEnum);
            if (StringUtils.isNotEmpty(value)) {
                return chain.proceed(setRequest(value, request, method));
            }
        }
        return chain.proceed(request);
    }

    private String getClazzNameByMethod(Method method) {
        return method.getDeclaringClass().getName();
    }

    private HttpUrl.Builder setNonSlashEndpoint(Request request, Method method) {
        String clazzName = getClazzNameByMethod(method);
        final RetrofitServiceBean currentServiceBean = super.context.getRetrofitServiceBean(clazzName);
        LinkedList<String> pathSegments = new LinkedList<>(request.url().pathSegments());
        String prefix = currentServiceBean.getRetrofitUrl().getRealPrefixUrl();
        String retrofitUrlPrefix = currentServiceBean.getRetrofitUrl().getRetrofitUrlPrefix();
        List<String> pathPrefixes = Arrays.asList(StringUtils.split(prefix, "/"));
        List<String> pathRetrofitUrlPrefixes = Arrays.asList(StringUtils.split(retrofitUrlPrefix, "/"));
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        pathSegments.forEach(path -> httpUrlBuilder.removePathSegment(0));
        pathPrefixes.forEach(httpUrlBuilder::addPathSegment);
        pathRetrofitUrlPrefixes.forEach(httpUrlBuilder::addPathSegment);
        pathSegments.forEach(httpUrlBuilder::addPathSegment);
        return httpUrlBuilder;
    }

    private String getHttpAnnotationValue(Method method, RetrofitHttpAnnotationEnum retrofitHttpAnnotationEnum) {
        String value;
        switch (retrofitHttpAnnotationEnum) {
            case HTTP_GET:
                value = method.getAnnotation(GET.class).value();
                break;
            case HTTP_POST:
                value = method.getAnnotation(POST.class).value();
                break;
            case HTTP_PUT:
                value = method.getAnnotation(PUT.class).value();
                break;
            case HTTP_DELETE:
                value = method.getAnnotation(DELETE.class).value();
                break;
            case HTTP_PATCH:
                value = method.getAnnotation(PATCH.class).value();
                break;
            case HTTP_OPTIONS:
                value = method.getAnnotation(OPTIONS.class).value();
                break;
            case HTTP_HEAD:
                value = method.getAnnotation(HEAD.class).value();
                break;
            case HTTP_HTTP:
                value = method.getAnnotation(HTTP.class).path();
                break;
            default:
                value = StringUtils.EMPTY;
                break;
        }
        return value;
    }

    private Request setRequest(String value, Request request, Method method) {
        if (value.startsWith(SLASH)) {
            // begin '/'，ignore prefix of baseUrl
            return request;
        } else {
            try {
                new URL(value);
                // full URL , do not need to proceed
                return request;
            } catch (MalformedURLException exception) {
                // overwrite URL predix
                final HttpUrl.Builder httpUrlBuilder = this.setNonSlashEndpoint(request, method);
                return request.newBuilder().url(httpUrlBuilder.build()).build();
            }
        }
    }

}
