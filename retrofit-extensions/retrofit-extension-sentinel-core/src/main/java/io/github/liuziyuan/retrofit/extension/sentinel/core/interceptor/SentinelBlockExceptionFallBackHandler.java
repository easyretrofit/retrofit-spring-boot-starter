package io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.BaseFallBack;
import io.github.liuziyuan.retrofit.extension.sentinel.core.RetrofitSentinelResourceContext;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelResource;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.FallBackBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.util.ResourceNameUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
            String conventionResourceName = ResourceNameUtil.getConventionResourceName(blockMethod);
            assert retrofitSentinelResource != null;
            var fallbackClazz = retrofitSentinelResource.fallback();
            if (fallbackClazz.getName().equals(BaseFallBack.class.getName())) {
                log.warn("Without the implementation of BaseFallback, there will be no degrade processing");
            } else {
                var fallBack = cdiBeanManager.getBean(fallbackClazz);
                try {
                    RetrofitSentinelResourceContext context = cdiBeanManager.getBean(RetrofitSentinelResourceContext.class);
                    SentinelExceptionBean exceptionBean = getSentinelResourceId(throwable);
                    FallBackBean fallBackBean = context.getFallBackBeanById(exceptionBean.getId());
                    if (fallBackBean != null) {
                        String fallBackMethodName = fallBackBean.getFallBackMethodName();
                        Method fallbackMethod = fallbackClazz.getDeclaredMethod(fallBackMethodName, blockMethod.getParameterTypes());
                        if (conventionResourceName.equals(fallBackBean.getDefaultResourceName())) {
                            return fallbackMethod.invoke(fallBack, args);
                        }
                    }
                    return null;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    private SentinelExceptionBean getSentinelResourceId(RetrofitExtensionException throwable) {
        SentinelExceptionBean bean = null;
        if (throwable.getCause() instanceof DegradeException) {
            DegradeException exception = (DegradeException) throwable.getCause();
            bean = new SentinelExceptionBean();
            bean.setId(exception.getRule().getId());
            bean.setResourceName(exception.getRule().getResource());
            return bean;
        }
        if (throwable.getCause() instanceof FlowException) {
            FlowException exception = (FlowException) throwable.getCause();
            bean = new SentinelExceptionBean();
            bean.setId(exception.getRule().getId());
            bean.setResourceName(exception.getRule().getResource());
            return bean;
        }
        return bean;
    }

    @Getter
    @Setter
    public class SentinelExceptionBean {
        private long id;
        private String resourceName;
    }
}
