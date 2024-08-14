package io.github.easyretrofit.spring.boot;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import io.github.easyretrofit.core.proxy.BaseExceptionDelegate;
import io.github.easyretrofit.core.proxy.ExceptionDelegate;
import io.github.easyretrofit.core.proxy.RetrofitServiceProxy;
import io.github.easyretrofit.core.resource.RetrofitApiServiceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import retrofit2.Retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The proxy factory of RetrofitServiceBean
 * SpringBoot的FactoryBean实现，在这里，从Spring上下文中获取Retrofit实例，生成代理对象
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitServiceProxyFactory<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> interfaceType;
    private ApplicationContext applicationContext;
    private final RetrofitApiServiceBean retrofitApiServiceBean;

    public RetrofitServiceProxyFactory(Class<T> interfaceType, RetrofitApiServiceBean retrofitApiServiceBean) {
        this.interfaceType = interfaceType;
        this.retrofitApiServiceBean = retrofitApiServiceBean;
    }

    @Override
    public T getObject() {
        String retrofitInstanceName = retrofitApiServiceBean.getRetrofitClientBeanInstanceName();
        Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates = new HashSet<>();
        Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegateSet = retrofitApiServiceBean.getExceptionDelegates();
        if (exceptionDelegateSet != null) {
            for (Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> entry : exceptionDelegateSet) {
                BaseExceptionDelegate<? extends RetrofitExtensionException> exceptionDelegate = applicationContext.getBean(entry);
                exceptionDelegates.add(exceptionDelegate);
            }
        }
        Retrofit retrofit = (Retrofit) applicationContext.getBean(retrofitInstanceName);
        InvocationHandler handler = new RetrofitServiceProxy<>(retrofit.create(interfaceType), exceptionDelegates);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
