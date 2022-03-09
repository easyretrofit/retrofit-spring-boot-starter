package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.annotation.*;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * generate RetrofitServiceBean object
 *
 * @author liuziyuan
 */
public class RetrofitServiceBeanGenerator implements Generator<RetrofitServiceBean> {
    private final Class<?> clazz;
    private final Environment environment;

    public RetrofitServiceBeanGenerator(Class<?> clazz, Environment environment) {
        this.clazz = clazz;
        this.environment = environment;
    }

    @Override
    public RetrofitServiceBean generate() {
        final Class<?> retrofitBuilderClazz = findParentRetrofitBuilderClazz(clazz);
        RetrofitServiceBean retrofitServiceBean = new RetrofitServiceBean();
        retrofitServiceBean.setSelfClazz(clazz);
        retrofitServiceBean.setParentClazz(retrofitBuilderClazz);
        RetrofitBuilder retrofitBuilderAnnotation = retrofitBuilderClazz.getAnnotation(RetrofitBuilder.class);
        retrofitServiceBean.setRetrofitBuilder(retrofitBuilderAnnotation);
        Set<RetrofitInterceptor> interceptors = this.getInterceptors(retrofitBuilderClazz);
        Set<RetrofitInterceptor> myInterceptors = this.getInterceptors(clazz);
        retrofitServiceBean.setMyInterceptors(myInterceptors);
        retrofitServiceBean.setInterceptors(interceptors);
        final RetrofitUrlPrefix retrofitUrlPrefix = clazz.getAnnotation(RetrofitUrlPrefix.class);
        final RetrofitDynamicBaseUrl retrofitDynamicBaseUrl = clazz.getAnnotation(RetrofitDynamicBaseUrl.class);
        String retrofitDynamicBaseUrlValue = retrofitDynamicBaseUrl == null ? null : retrofitDynamicBaseUrl.value();
        RetrofitUrl url = new RetrofitUrl(retrofitBuilderAnnotation.baseUrl(),
                retrofitDynamicBaseUrlValue,
                retrofitUrlPrefix == null ? null : retrofitUrlPrefix.value(),
                environment);
        retrofitServiceBean.setRetrofitUrl(url);
        return retrofitServiceBean;
    }

    private Class<?> findParentRetrofitBuilderClazz(Class<?> clazz) {
        RetrofitBuilder retrofitBuilder = clazz.getAnnotation(RetrofitBuilder.class);
        Class<?> targetClazz = clazz;
        if (retrofitBuilder == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                targetClazz = findParentRetrofitBuilderClazz(interfaces[0]);
            }
        }
        return targetClazz;
    }

    private Set<RetrofitInterceptor> getInterceptors(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        Set<RetrofitInterceptor> retrofitInterceptorAnnotations = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Interceptors) {
                RetrofitInterceptor[] values = ((Interceptors) annotation).value();
                Collections.addAll(retrofitInterceptorAnnotations, values);
            } else if (annotation instanceof RetrofitInterceptor) {
                retrofitInterceptorAnnotations.add((RetrofitInterceptor) annotation);
            }
        }
        return retrofitInterceptorAnnotations;
    }


}
