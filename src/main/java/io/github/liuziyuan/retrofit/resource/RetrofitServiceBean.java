package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Each API interface file corresponds to a RetrofitServiceBean
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
