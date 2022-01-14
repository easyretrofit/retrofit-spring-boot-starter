package com.github.liuziyuan.retrofit.model;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;

import java.util.List;

/**
 * @author liuziyuan
 * @date 12/31/2021 5:54 PM
 */
public class RetrofitServiceBean {

    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private RetrofitUrl retrofitUrl;
    private RetrofitBuilder retrofitBuilder;
    private List<RetrofitInterceptor> interceptors;

    public void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBean = retrofitClientBean;
        this.retrofitBuilder = retrofitClientBean.getRetrofitBuilder();
        this.interceptors = retrofitClientBean.getInterceptors();
    }

    private RetrofitClientBean retrofitClientBean;

    public RetrofitClientBean getRetrofitClientBean() {
        return retrofitClientBean;
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


    public RetrofitBuilder getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public void setRetrofitBuilder(RetrofitBuilder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public List<RetrofitInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<RetrofitInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

}
