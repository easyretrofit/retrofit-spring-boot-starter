package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.util.UniqueKeyUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Each API interface file corresponds to a RetrofitServiceBean
 *
 * @author liuziyuan
 */
public class RetrofitApiServiceBean implements UniqueKey {

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

    void setSelfClazz(Class<?> selfClazz) {
        this.selfClazz = selfClazz;
    }

    public Class<?> getParentClazz() {
        return parentClazz;
    }

    void setParentClazz(Class<?> parentClazz) {
        this.parentClazz = parentClazz;
    }

    public RetrofitUrl getRetrofitUrl() {
        return retrofitUrl;
    }

    void setRetrofitUrl(RetrofitUrl retrofitUrl) {
        this.retrofitUrl = retrofitUrl;
    }

    public RetrofitBuilderBean getRetrofitBuilder() {
        return retrofitBuilder;
    }

    void setRetrofitBuilder(RetrofitBuilderBean retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public Set<RetrofitInterceptorBean> getInterceptors() {
        return interceptors;
    }

    void setInterceptors(Set<RetrofitInterceptorBean> interceptors) {
        this.interceptors = interceptors;
    }

    public Set<RetrofitInterceptorBean> getMyInterceptors() {
        return myInterceptors;
    }

    void setMyInterceptors(Set<RetrofitInterceptorBean> myInterceptors) {
        this.myInterceptors = myInterceptors;
    }

    public String getRetrofitClientBeanInstanceName() {
        return retrofitClientBeanInstanceName;
    }

    public Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> getExceptionDelegates() {
        return exceptionDelegates;
    }

    @Override
    public String toString() {
        String interceptorStr = null;
        if (interceptors != null) {
            interceptorStr = interceptors.stream().map(RetrofitInterceptorBean::generateUniqueKey).collect(Collectors.joining(","));
        }
        String myInterceptorStr = null;
        if (myInterceptors != null) {
            myInterceptorStr = myInterceptors.stream().map(RetrofitInterceptorBean::generateUniqueKey).collect(Collectors.joining(","));
        }
        String exceptionDelegateStr = null;
        if (exceptionDelegates != null) {
            exceptionDelegateStr = exceptionDelegates.stream().map(Class::getName).collect(Collectors.joining(","));
        }
        return "RetrofitApiServiceBean{" +
                "selfClazz=" + selfClazz +
                ", parentClazz=" + parentClazz +
                ", retrofitUrl=" + retrofitUrl +
                ", retrofitBuilder=" + retrofitBuilder +
                ", interceptors=" + interceptorStr +
                ", myInterceptors=" + myInterceptorStr +
                ", exceptionDelegates=" + exceptionDelegateStr +
                '}';
    }

    @Override
    public String generateUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }
}
