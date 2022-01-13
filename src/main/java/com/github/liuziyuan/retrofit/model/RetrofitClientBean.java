package com.github.liuziyuan.retrofit.model;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author liuziyuan
 * @date 12/31/2021 3:46 PM
 */
public class RetrofitClientBean {

    private String retrofitInstanceName;
    private String realHostUrl;
    private RetrofitBuilder retrofitBuilder;
    private List<RetrofitInterceptor> interceptors;
    private List<RetrofitServiceBean> retrofitServices;

    public RetrofitClientBean() {
        this.interceptors = new ArrayList<>();
        this.retrofitServices = new ArrayList<>();
    }

    void setRetrofitInstanceName(String retrofitInstanceName) {
        this.retrofitInstanceName = retrofitInstanceName + "-" + UUID.randomUUID();
    }

    void setRealHostUrl(String realHostUrl) {
        this.realHostUrl = realHostUrl;
    }

    void setRetrofitBuilder(RetrofitBuilder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    void setInterceptors(List<RetrofitInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public String getRetrofitInstanceName() {
        return retrofitInstanceName;
    }

    public String getRealHostUrl() {
        return realHostUrl;
    }

    public RetrofitBuilder getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public List<RetrofitInterceptor> getInterceptors() {
        return interceptors;
    }

    public List<RetrofitServiceBean> getRetrofitServices() {
        return retrofitServices;
    }

    void addRetrofitServiceBean(RetrofitServiceBean retrofitServiceBean) {
        retrofitServices.add(retrofitServiceBean);
        retrofitServiceBean.setRetrofitClientBean(this);
    }

}
