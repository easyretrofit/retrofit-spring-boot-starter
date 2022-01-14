package com.github.liuziyuan.retrofit.resource;

import com.github.liuziyuan.retrofit.annotation.Interceptors;
import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import com.github.liuziyuan.retrofit.Handler;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liuziyuan
 * @date 1/6/2022 3:54 PM
 */
public class RetrofitServiceBeanHandler implements Handler<RetrofitServiceBean> {
    private Class<?> clazz;
    private Environment environment;

    public RetrofitServiceBeanHandler(Class<?> clazz, Environment environment) {
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
        List<RetrofitInterceptor> interceptors = this.getInterceptors(retrofitBuilderClazz);
        retrofitServiceBean.setInterceptors(interceptors);
        RetrofitUrl url = new RetrofitUrl(retrofitBuilderAnnotation.baseUrl(), environment);
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

    private List<RetrofitInterceptor> getInterceptors(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        List<RetrofitInterceptor> retrofitInterceptorAnnotations = new ArrayList<>();
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
