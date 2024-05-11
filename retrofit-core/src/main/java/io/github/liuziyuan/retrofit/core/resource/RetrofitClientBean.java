package io.github.liuziyuan.retrofit.core.resource;

import java.util.*;

/**
 * RetrofitServiceBean with the same @RetrofitBuilder are aggregated into RetrofitClientBean
 *
 * @author liuziyuan
 */

public class RetrofitClientBean {

    private String retrofitInstanceName;
    private String realHostUrl;
    private UrlStatus urlStatus;
    private RetrofitBuilderBean retrofitBuilder;
    private Set<RetrofitInterceptorBean> interceptors;
    private Set<RetrofitInterceptorBean> inheritedInterceptors;
    private List<RetrofitApiServiceBean> retrofitApiServiceBeans;


    public RetrofitClientBean() {
        this.interceptors = new LinkedHashSet<>();
        this.inheritedInterceptors = new LinkedHashSet<>();
        this.retrofitApiServiceBeans = new ArrayList<>();
    }

    public void setRetrofitInstanceName(String retrofitInstanceName) {
        this.retrofitInstanceName = retrofitInstanceName + "@" + UUID.randomUUID();
    }

    public void addRetrofitServiceBean(RetrofitApiServiceBean retrofitApiServiceBean) {
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

    public void setRealHostUrl(String realHostUrl) {
        this.realHostUrl = realHostUrl;
    }

    public UrlStatus getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(UrlStatus urlStatus) {
        this.urlStatus = urlStatus;
    }

    public RetrofitBuilderBean getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public void setRetrofitBuilder(RetrofitBuilderBean retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public Set<RetrofitInterceptorBean> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Set<RetrofitInterceptorBean> interceptors) {
        this.interceptors = interceptors;
    }

    public Set<RetrofitInterceptorBean> getInheritedInterceptors() {
        return inheritedInterceptors;
    }

    public void setInheritedInterceptors(Set<RetrofitInterceptorBean> inheritedInterceptors) {
        this.inheritedInterceptors = inheritedInterceptors;
    }

    public List<RetrofitApiServiceBean> getRetrofitApiServiceBeans() {
        return retrofitApiServiceBeans;
    }

    public void setRetrofitApiServiceBeans(List<RetrofitApiServiceBean> retrofitApiServiceBeans) {
        this.retrofitApiServiceBeans = retrofitApiServiceBeans;
    }

    @Override
    public String toString() {
        return "RetrofitClientBean{" +
                "retrofitInstanceName='" + retrofitInstanceName + '\'' +
                ", realHostUrl='" + realHostUrl + '\'' +
                ", urlStatus=" + urlStatus +
                ", retrofitBuilder=" + retrofitBuilder +
                ", interceptors=" + interceptors +
                ", inheritedInterceptors=" + inheritedInterceptors +
                ", retrofitApiServiceBeans=" + retrofitApiServiceBeans +
                '}';
    }
}
