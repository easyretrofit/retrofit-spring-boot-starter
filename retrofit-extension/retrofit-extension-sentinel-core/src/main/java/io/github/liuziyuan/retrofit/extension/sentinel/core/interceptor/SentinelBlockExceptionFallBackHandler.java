package io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor;

import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.BaseFallBack;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelResource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import retrofit2.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class SentinelBlockExceptionFallBackHandler extends BaseExceptionDelegate<SentinelBlockException> {

    private final CDIBeanManager cdiBeanManager;
    public SentinelBlockExceptionFallBackHandler(Class<SentinelBlockException> exceptionClassName, CDIBeanManager cdiBeanManager) {
        super(exceptionClassName);
        this.cdiBeanManager = cdiBeanManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, RetrofitExtensionException throwable) {
        if (throwable instanceof SentinelBlockException) {
            RetrofitApiServiceBean retrofitApiServiceBean = throwable.getRetrofitApiServiceBean();
            RetrofitSentinelResource retrofitSentinelResource = retrofitApiServiceBean.getAnnotationResource(RetrofitSentinelResource.class);
            Request request = throwable.getRequest();
            Method blockMethod = Objects.requireNonNull(request.tag(Invocation.class)).method();
            assert retrofitSentinelResource != null;
            Class<? extends BaseFallBack> fallbackClazz = retrofitSentinelResource.fallback();
            if (fallbackClazz.getName().equals(BaseFallBack.class.getName())) {
                log.warn("Without the implementation of BaseFallback, there will be no degrade processing");
            } else {
                BaseFallBack fallBack = cdiBeanManager.getBean(fallbackClazz);
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
