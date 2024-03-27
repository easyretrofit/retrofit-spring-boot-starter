package io.github.liuziyuan.retrofit.extension.sentinel.spring.boot;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;

import io.github.liuziyuan.retrofit.extension.sentinel.core.BaseFallBack;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelResource;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.SentinelBlockException;
import io.github.liuziyuan.retrofit.spring.boot.util.SpringContextUtil;


import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import retrofit2.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class SentinelBlockExceptionFallBackHandler extends BaseExceptionDelegate<SentinelBlockException> {
    public SentinelBlockExceptionFallBackHandler(Class<SentinelBlockException> exceptionClassName) {
        super(exceptionClassName);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, RetrofitExtensionException throwable) {
        if (throwable instanceof SentinelBlockException) {
            RetrofitApiServiceBean retrofitApiServiceBean = throwable.getRetrofitApiServiceBean();
            RetrofitSentinelResource retrofitSentinelResource = retrofitApiServiceBean.getAnnotationResource(RetrofitSentinelResource.class);
            Request request = throwable.getRequest();
            Method blockMethod = Objects.requireNonNull(request.tag(Invocation.class)).method();
            Class<? extends BaseFallBack> fallbackClazz = retrofitSentinelResource.fallback();
            if (fallbackClazz.getName().equals(BaseFallBack.class.getName())) {
                log.warn("Without the implementation of BaseFallback, there will be no degrade processing");
            } else {
                BaseFallBack fallBack = SpringContextUtil.getBean(fallbackClazz);
                try {
                    Method fallbackMethod = fallbackClazz.getDeclaredMethod(blockMethod.getName(), blockMethod.getParameterTypes());
                    if (fallbackMethod.getName().equals(method.getName())) {
                        return fallbackMethod.invoke(fallBack, args);
                    }
                    return null;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
