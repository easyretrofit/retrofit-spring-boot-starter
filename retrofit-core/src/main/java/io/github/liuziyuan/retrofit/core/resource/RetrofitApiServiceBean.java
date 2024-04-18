package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Each API interface file corresponds to a RetrofitServiceBean
 *
 * @author liuziyuan
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public final class RetrofitApiServiceBean {

    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private RetrofitUrl retrofitUrl;
    private RetrofitBuilderBean retrofitBuilder;
    /**
     * parent Interface interceptors
     */
    private Set<RetrofitInterceptorBean> interceptors;
    private Set<RetrofitInterceptorBean> myInterceptors;
    private RetrofitClientBean retrofitClientBean;
    private Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegates;

    public void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBean = retrofitClientBean;
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

}
