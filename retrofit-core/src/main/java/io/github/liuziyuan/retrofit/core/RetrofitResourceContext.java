package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * The top structure contains all the objects created in the starter
 *
 * @author liuziyuan
 */
@Getter
@Setter
public class RetrofitResourceContext {

    private String[] basePackages;
    private Class<?> retrofitBuilderExtensionClazz;
    private List<Class<?>> interceptorExtensionsClasses;
    private List<RetrofitClientBean> retrofitClients;
    private Map<String, RetrofitApiServiceBean> retrofitApiServices;

    public RetrofitResourceContext() {

    }

    public RetrofitResourceContext(String[] basePackages,
                                   List<RetrofitClientBean> retrofitClients,
                                   Map<String, RetrofitApiServiceBean> retrofitApiServices,
                                   Class<?> retrofitBuilderExtensionClazz,
                                   List<Class<?>> interceptorExtensionsClasses) {
        this.retrofitApiServices = retrofitApiServices;
        this.retrofitClients = retrofitClients;
        this.basePackages = basePackages;
        this.retrofitBuilderExtensionClazz = retrofitBuilderExtensionClazz;
        this.interceptorExtensionsClasses = interceptorExtensionsClasses;
    }

    public List<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

    public RetrofitApiServiceBean getRetrofitApiServiceBean(String clazzFullName) {
        return retrofitApiServices.get(clazzFullName);
    }

    public RetrofitApiServiceBean getRetrofitApiServiceBean(Class<?> clazz) {
        return retrofitApiServices.get(clazz.getName());
    }

}
