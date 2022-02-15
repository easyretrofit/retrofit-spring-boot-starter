package io.github.liuziyuan.retrofit.proxy;

import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
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
 * @author liuziyuan
 */
@Slf4j
public class RetrofitServiceProxyFactory<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> interfaceType;
    private ApplicationContext applicationContext;
    private final RetrofitServiceBean retrofitServiceBean;

    public RetrofitServiceProxyFactory(Class<T> interfaceType, RetrofitServiceBean retrofitServiceBean) {
        this.interfaceType = interfaceType;
        this.retrofitServiceBean = retrofitServiceBean;
    }

    @Override
    public T getObject() {
        String retrofitInstanceName = retrofitServiceBean.getRetrofitClientBean().getRetrofitInstanceName();
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
