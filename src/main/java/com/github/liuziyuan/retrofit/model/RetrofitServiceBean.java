package com.github.liuziyuan.retrofit.model;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;

import java.util.ArrayList;
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
    private RetrofitClientBean retrofitClientBean;

    public RetrofitClientBean getRetrofitClientBean() {
        return retrofitClientBean;
    }

    void setRetrofitClientBean() {
        this.interceptors = new ArrayList<>();
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


    public RetrofitBuilder getRetrofitBuilder() {
        return retrofitBuilder;
    }

    void setRetrofitBuilder(RetrofitBuilder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public List<RetrofitInterceptor> getInterceptors() {
        return interceptors;
    }

    void setInterceptors(List<RetrofitInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

}
