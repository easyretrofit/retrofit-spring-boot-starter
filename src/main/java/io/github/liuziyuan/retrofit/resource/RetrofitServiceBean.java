package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitCloudService;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Each API interface file corresponds to a RetrofitServiceBean
 *
 * @author liuziyuan
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class RetrofitServiceBean {

    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private RetrofitUrl retrofitUrl;
    private RetrofitCloudService retrofitCloudService;
    private RetrofitBuilder retrofitBuilder;
    /**
     * parent Interface interceptors
     */
    private Set<RetrofitInterceptor> interceptors;
    private Set<RetrofitInterceptor> myInterceptors;
    private RetrofitClientBean retrofitClientBean;

    public void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBean = retrofitClientBean;
        this.retrofitBuilder = retrofitClientBean.getRetrofitBuilder();
        this.interceptors = retrofitClientBean.getInterceptors();
    }

}
