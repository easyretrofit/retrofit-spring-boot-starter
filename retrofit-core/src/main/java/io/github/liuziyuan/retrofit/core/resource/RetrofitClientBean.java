package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.util.UniqueKeyUtils;
import retrofit2.Retrofit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RetrofitServiceBean with the same @RetrofitBuilder are aggregated into RetrofitClientBean
 *
 * @author liuziyuan
 */

public class RetrofitClientBean implements UniqueKey {

    private String retrofitInstanceName;
    private String realHostUrl;
    private UrlStatus urlStatus;
    private RetrofitBuilderBean retrofitBuilder;
    private Set<RetrofitInterceptorBean> interceptors;
    private Set<RetrofitInterceptorBean> inheritedInterceptors;
    private List<RetrofitApiServiceBean> retrofitApiServiceBeans;


    public RetrofitClientBean(RetrofitApiServiceBean serviceBean) {
        this.interceptors = new LinkedHashSet<>();
        this.inheritedInterceptors = new LinkedHashSet<>();
        this.retrofitApiServiceBeans = new ArrayList<>();
        this.setRetrofitBuilder(serviceBean.getRetrofitBuilder());
        this.setRealHostUrl(serviceBean.getRetrofitUrl().getDefaultUrl().getRealHostUrl());
        this.setUrlStatus(serviceBean.getRetrofitUrl().getUrlStatus());
        this.setInterceptors(serviceBean.getInterceptors());
        this.addInheritedInterceptors(serviceBean.getMyInterceptors());
        this.setRetrofitInstanceName();
    }

    public void addRetrofitApiServiceBean(RetrofitApiServiceBean retrofitApiServiceBean) {
        retrofitApiServiceBeans.add(retrofitApiServiceBean);
        retrofitApiServiceBean.setRetrofitClientBean(this);
    }

    public void addInheritedInterceptors(Set<RetrofitInterceptorBean> serviceInheritedInterceptors) {
        inheritedInterceptors.addAll(serviceInheritedInterceptors);
    }

    public String getRetrofitInstanceName() {
        return retrofitInstanceName;
    }

    public String getRealHostUrl() {
        return realHostUrl;
    }

    void setRealHostUrl(String realHostUrl) {
        this.realHostUrl = realHostUrl;
    }

    public UrlStatus getUrlStatus() {
        return urlStatus;
    }

    void setUrlStatus(UrlStatus urlStatus) {
        this.urlStatus = urlStatus;
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

    public Set<RetrofitInterceptorBean> getInheritedInterceptors() {
        return inheritedInterceptors;
    }

    void setInheritedInterceptors(Set<RetrofitInterceptorBean> inheritedInterceptors) {
        this.inheritedInterceptors = inheritedInterceptors;
    }

    public List<RetrofitApiServiceBean> getRetrofitApiServiceBeans() {
        return retrofitApiServiceBeans;
    }

    void setRetrofitApiServiceBeans(List<RetrofitApiServiceBean> retrofitApiServiceBeans) {
        this.retrofitApiServiceBeans = retrofitApiServiceBeans;
    }

    @Override
    public String toString() {
        String interceptorsStr = null;
        if (interceptors != null) {
            interceptorsStr = interceptors.stream().map(RetrofitInterceptorBean::toString).collect(Collectors.joining(","));
        }
        String inheritedInterceptorsStr = null;
        if (inheritedInterceptors != null) {
            inheritedInterceptorsStr = inheritedInterceptors.stream().map(RetrofitInterceptorBean::toString).collect(Collectors.joining(","));
        }

        return "RetrofitClientBean{" +
                "realHostUrl='" + realHostUrl + '\'' +
                ", urlStatus=" + urlStatus.toString() +
                ", retrofitBuilder=" + retrofitBuilder.toString() +
                ", interceptors=" + interceptorsStr +
                ", inheritedInterceptors=" + inheritedInterceptorsStr +
                '}';
    }

    @Override
    public String generateUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }

    /**
     * set retrofit instance name when RetrofitClientBean instance created, the retrofitInstanceName need whole attributes filled.
     * important
     */
    public void setRetrofitInstanceName() {
        this.retrofitInstanceName = Retrofit.class.getSimpleName().concat("@" + this.generateUniqueKey());
    }
}
