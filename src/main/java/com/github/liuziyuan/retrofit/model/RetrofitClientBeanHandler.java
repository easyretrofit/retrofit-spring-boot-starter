package com.github.liuziyuan.retrofit.model;

import com.github.liuziyuan.retrofit.annotation.Interceptors;
import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import org.springframework.core.env.Environment;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuziyuan
 * @date 1/6/2022 3:53 PM
 */
public class RetrofitClientBeanHandler implements Handler<RetrofitClientBean> {
    private Class<?> retrofitBuilderClass;
    private Environment environment;

    public RetrofitClientBeanHandler(Class<?> retrofitBuilderClass, Environment environment) {
        this.retrofitBuilderClass = retrofitBuilderClass;
        this.environment = environment;
    }

    @Override
    public RetrofitClientBean generate() {
        final RetrofitBuilder retrofitBuilderAnnotation = retrofitBuilderClass.getAnnotation(RetrofitBuilder.class);
        if (retrofitBuilderAnnotation != null) {
            RetrofitClientBean retrofitClientBean = new RetrofitClientBean();
            retrofitClientBean.setRetrofitInstanceName(Retrofit.class.getSimpleName());
            retrofitClientBean.setRetrofitBuilder(retrofitBuilderAnnotation);
            retrofitClientBean.setSelfClazz(retrofitBuilderClass);
            final List<RetrofitInterceptor> interceptors = this.getInterceptors(retrofitBuilderClass);
            retrofitClientBean.setInterceptors(interceptors);
            RetrofitUrl url = new RetrofitUrl(retrofitBuilderAnnotation.baseUrl(), environment);
            retrofitClientBean.setUrl(url);
            return retrofitClientBean;
        }
        return null;
    }

    private List<RetrofitInterceptor> getInterceptors(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        List<RetrofitInterceptor> retrofitInterceptorAnnotations = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Interceptors) {
                RetrofitInterceptor[] values = ((Interceptors) annotation).value();
                for (RetrofitInterceptor interceptor : values) {
                    retrofitInterceptorAnnotations.add(interceptor);
                }
            } else if (annotation instanceof RetrofitInterceptor) {
                retrofitInterceptorAnnotations.add((RetrofitInterceptor) annotation);
            }
        }
        return retrofitInterceptorAnnotations;
    }

}
