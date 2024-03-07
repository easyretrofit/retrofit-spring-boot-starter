package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.proxy.RetrofitServiceProxy;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import retrofit2.Retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * The proxy factory of RetrofitServiceBean
 * SpringBoot的FactoryBean实现，在这里，从Spring上下文中获取Retrofit实例，生成代理对象
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
        String retrofitInstanceName = retrofitApiServiceBean.getRetrofitClientBean().getRetrofitInstanceName();
        Retrofit retrofit = (Retrofit) applicationContext.getBean(retrofitInstanceName);
        T t = retrofit.create(interfaceType);
        InvocationHandler handler = new RetrofitServiceProxy<>(t);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
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
