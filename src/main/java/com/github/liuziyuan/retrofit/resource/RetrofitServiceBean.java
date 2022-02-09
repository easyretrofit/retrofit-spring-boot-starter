package com.github.liuziyuan.retrofit.resource;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author liuziyuan
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class RetrofitServiceBean {

    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private RetrofitUrl retrofitUrl;
    private RetrofitBuilder retrofitBuilder;
    private List<RetrofitInterceptor> interceptors;
    private RetrofitClientBean retrofitClientBean;

    public void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBean = retrofitClientBean;
        this.retrofitBuilder = retrofitClientBean.getRetrofitBuilder();
        this.interceptors = retrofitClientBean.getInterceptors();
    }



}
