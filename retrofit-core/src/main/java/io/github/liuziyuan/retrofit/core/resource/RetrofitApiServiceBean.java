package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Each API interface file corresponds to a RetrofitServiceBean
 *
 * @author liuziyuan
 */
public class RetrofitApiServiceBean {

    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private RetrofitUrl retrofitUrl;
    private RetrofitBuilderBean retrofitBuilder;
    /**
     * parent Interface interceptors
     */
    private Set<RetrofitInterceptorBean> interceptors;
    private Set<RetrofitInterceptorBean> myInterceptors;
    private String retrofitClientBeanInstanceName;
    private Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegates;


    public RetrofitApiServiceBean() {
    }
    public void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBeanInstanceName = retrofitClientBean.getRetrofitInstanceName();
        this.retrofitBuilder = retrofitClientBean.getRetrofitBuilder();
        this.interceptors = retrofitClientBean.getInterceptors();
    }

    public void addExceptionDelegate(Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> clazz) {
        if (exceptionDelegates == null) {
            exceptionDelegates = new HashSet<>();
        }
        exceptionDelegates.add(clazz);
    }

    public <T extends Annotation> T getAnnotationResource(Class<? extends Annotation> clazz) {
        Class<?> selfClazz = this.getSelfClazz();
        Class<?> parentClazz = this.getParentClazz();
        T annotationInSelf = (T) selfClazz.getDeclaredAnnotation(clazz);
        if (annotationInSelf != null) {
            return annotationInSelf;
        }

        if (parentClazz != null) {
            T annotationInParent = (T) parentClazz.getDeclaredAnnotation(clazz);
            if (annotationInParent != null) {
                return annotationInParent;
            }
        }

        return null;
    }

    public Class<?> getSelfClazz() {
        return selfClazz;
    }

    public void setSelfClazz(Class<?> selfClazz) {
        this.selfClazz = selfClazz;
    }

    public Class<?> getParentClazz() {
        return parentClazz;
    }

    public void setParentClazz(Class<?> parentClazz) {
        this.parentClazz = parentClazz;
    }

    public RetrofitUrl getRetrofitUrl() {
        return retrofitUrl;
    }

    public void setRetrofitUrl(RetrofitUrl retrofitUrl) {
        this.retrofitUrl = retrofitUrl;
    }

    public RetrofitBuilderBean getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public void setRetrofitBuilder(RetrofitBuilderBean retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public Set<RetrofitInterceptorBean> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Set<RetrofitInterceptorBean> interceptors) {
        this.interceptors = interceptors;
    }

    public Set<RetrofitInterceptorBean> getMyInterceptors() {
        return myInterceptors;
    }

    public void setMyInterceptors(Set<RetrofitInterceptorBean> myInterceptors) {
        this.myInterceptors = myInterceptors;
    }

    public String getRetrofitClientBeanInstanceName() {
        return retrofitClientBeanInstanceName;
    }

    public void setRetrofitClientBeanInstanceName(String retrofitClientBeanInstanceName) {
        this.retrofitClientBeanInstanceName = retrofitClientBeanInstanceName;
    }

    public Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> getExceptionDelegates() {
        return exceptionDelegates;
    }

    public void setExceptionDelegates(Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegates) {
        this.exceptionDelegates = exceptionDelegates;
    }
}
