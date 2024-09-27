package io.github.easyretrofit.spring.boot;

import io.github.easyretrofit.core.delegate.ExceptionDelegateSetGenerator;
import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;
import io.github.easyretrofit.core.proxy.JdkDynamicProxy;
import io.github.easyretrofit.core.proxy.RetrofitApiInterfaceInvocationHandler;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import retrofit2.Retrofit;

import java.lang.reflect.InvocationHandler;
import java.util.Set;
import java.util.function.Function;

/**
 * The proxy factory of RetrofitApiInterfaceBean
 * SpringBoot的FactoryBean实现，在这里，从Spring上下文中获取Retrofit实例，生成代理对象
 *
 * @author liuziyuan
 */
public class RetrofitServiceProxyFactory<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> interfaceType;
    private ApplicationContext applicationContext;
    private final RetrofitApiInterfaceBean retrofitApiInterfaceBean;

    public RetrofitServiceProxyFactory(Class<T> interfaceType, RetrofitApiInterfaceBean RetrofitApiInterfaceBean) {
        this.interfaceType = interfaceType;
        this.retrofitApiInterfaceBean = RetrofitApiInterfaceBean;
    }

    @Override
    public T getObject() {
        String retrofitInstanceName = retrofitApiInterfaceBean.getRetrofitClientBeanInstanceName();
        Function<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>, BaseExceptionDelegate<? extends RetrofitExtensionException>> function = t -> applicationContext.getBean(t);
        Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates = ExceptionDelegateSetGenerator.generate(retrofitApiInterfaceBean.getExceptionDelegates(), function);
//        Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegateSet = RetrofitApiInterfaceBean.getExceptionDelegates();
//        if (exceptionDelegateSet != null) {
//            for (Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> entry : exceptionDelegateSet) {
//                BaseExceptionDelegate<? extends RetrofitExtensionException> exceptionDelegate = applicationContext.getBean(entry);
//                exceptionDelegates.add(exceptionDelegate);
//            }
//        }
        Retrofit retrofit = (Retrofit) applicationContext.getBean(retrofitInstanceName);
        InvocationHandler handler = new RetrofitApiInterfaceInvocationHandler<>(retrofit.create(interfaceType), exceptionDelegates);
        return JdkDynamicProxy.create(interfaceType.getClassLoader(), new Class[]{interfaceType}, handler);
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
