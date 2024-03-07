package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitBuilderBean;
import io.github.liuziyuan.retrofit.core.resource.UrlStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

/**
 * RetrofitServiceBean with the same @RetrofitBuilder are aggregated into RetrofitClientBean
 *
 * @author liuziyuan
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public class RetrofitClientBean {

    private String retrofitInstanceName;
    private String realHostUrl;
    private UrlStatus urlStatus;
    private RetrofitBuilderBean retrofitBuilder;
    private Set<RetrofitInterceptor> interceptors;
    private Set<RetrofitInterceptor> inheritedInterceptors;
    private List<io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean> retrofitServices;

    public RetrofitClientBean() {
        this.interceptors = new LinkedHashSet<>();
        this.inheritedInterceptors = new LinkedHashSet<>();
        this.retrofitServices = new ArrayList<>();
    }

    public void setRetrofitInstanceName(String retrofitInstanceName) {
        this.retrofitInstanceName = retrofitInstanceName + "-" + UUID.randomUUID();
    }

    public void addRetrofitServiceBean(RetrofitApiServiceBean retrofitApiServiceBean) {
        retrofitServices.add(retrofitApiServiceBean);
        retrofitApiServiceBean.setRetrofitClientBean(this);
    }

    public void addInheritedInterceptors(Set<RetrofitInterceptor> serviceInheritedInterceptors) {
        inheritedInterceptors.addAll(serviceInheritedInterceptors);
    }

}