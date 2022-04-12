package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.extension.BaseCallAdapterFactoryBuilder;
import io.github.liuziyuan.retrofit.extension.BaseConverterFactoryBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuziyuan
 */
public class RetrofitResourceComparer {

    private final RetrofitClientBean clientBean;
    private final RetrofitServiceBean serviceBean;

    public RetrofitResourceComparer(RetrofitClientBean clientBean, RetrofitServiceBean serviceBean) {
        this.clientBean = clientBean;
        this.serviceBean = serviceBean;
    }

    public boolean okHttpClientInstanceCompare() {
        final String clientBeanRetrofitBuilderOkHttpClientSimpleName = clientBean.getRetrofitBuilder().client().getName();
        final String serviceBeanRetrofitBuilderOkHttpClientSimpleName = serviceBean.getRetrofitBuilder().client().getName();
        return StringUtils.equals(clientBeanRetrofitBuilderOkHttpClientSimpleName, serviceBeanRetrofitBuilderOkHttpClientSimpleName);
    }

    public boolean callBackExecutorCompare() {
        final String clientBeanCallBackExecutorName = clientBean.getRetrofitBuilder().callbackExecutor().getName();
        final String serviceBeanCallBackExecutorName = serviceBean.getRetrofitBuilder().callbackExecutor().getName();
        return StringUtils.equals(clientBeanCallBackExecutorName, serviceBeanCallBackExecutorName);
    }

    public boolean callFactoryCompare() {
        final String clientBeanCallFactoryName = clientBean.getRetrofitBuilder().callFactory().getName();
        final String serviceBeanCallFactoryName = serviceBean.getRetrofitBuilder().callFactory().getName();
        return StringUtils.equals(clientBeanCallFactoryName, serviceBeanCallFactoryName);
    }

    public boolean validateEagerlyCompare() {
        final boolean clientBeanValidateEagerly = clientBean.getRetrofitBuilder().validateEagerly();
        final boolean serviceBeanValidateEagerly = serviceBean.getRetrofitBuilder().validateEagerly();
        return clientBeanValidateEagerly == serviceBeanValidateEagerly;
    }

    public boolean callAdapterFactoryCompare() {
        List<String> clientBeanCallAdapterFactoryList = new ArrayList<>();
        for (Class<? extends BaseCallAdapterFactoryBuilder> clazz : clientBean.getRetrofitBuilder().addCallAdapterFactory()) {
            clientBeanCallAdapterFactoryList.add(clazz.getSimpleName());
        }
        List<String> serviceBeanCallAdapterFactoryList = new ArrayList<>();
        for (Class<? extends BaseCallAdapterFactoryBuilder> clazz : serviceBean.getRetrofitBuilder().addCallAdapterFactory()) {
            serviceBeanCallAdapterFactoryList.add(clazz.getSimpleName());
        }
        return clientBeanCallAdapterFactoryList.containsAll(serviceBeanCallAdapterFactoryList) && serviceBeanCallAdapterFactoryList.containsAll(clientBeanCallAdapterFactoryList);
    }

    public boolean converterFactoryCompare() {
        List<String> clientBeanConverterFactoryList = new ArrayList<>();
        for (Class<? extends BaseConverterFactoryBuilder> clazz : clientBean.getRetrofitBuilder().addConverterFactory()) {
            clientBeanConverterFactoryList.add(clazz.getSimpleName());
        }
        List<String> serviceBeanConverterFactoryList = new ArrayList<>();
        for (Class<? extends BaseConverterFactoryBuilder> clazz : serviceBean.getRetrofitBuilder().addConverterFactory()) {
            serviceBeanConverterFactoryList.add(clazz.getSimpleName());
        }
        return clientBeanConverterFactoryList.containsAll(serviceBeanConverterFactoryList) && serviceBeanConverterFactoryList.containsAll(clientBeanConverterFactoryList);
    }

    public boolean interceptorsCompare() {
        List<String> clientBeanInterceptorSimpleNameList = new ArrayList<>();
        clientBean.getInterceptors().forEach(i -> clientBeanInterceptorSimpleNameList.add(i.handler().getSimpleName()));
        List<String> serviceBeanInterceptorSimpleNameList = new ArrayList<>();
        serviceBean.getInterceptors().forEach(i -> serviceBeanInterceptorSimpleNameList.add(i.handler().getSimpleName()));
        return clientBeanInterceptorSimpleNameList.containsAll(serviceBeanInterceptorSimpleNameList) &&
                serviceBeanInterceptorSimpleNameList.containsAll(clientBeanInterceptorSimpleNameList);
    }

    public boolean hostUrlCompare() {
        return StringUtils.equals(serviceBean.getRetrofitUrl().getDefaultUrl().getRealHostUrl(), clientBean.getRealHostUrl());
    }

    public boolean isDummyUrlCompare() {
        return serviceBean.getRetrofitUrl().getUrlStatus().equals(clientBean.getUrlStatus());
    }


    public boolean isSameRetrofitBuilderInstance() {
        return okHttpClientInstanceCompare() &&
                callBackExecutorCompare() &&
                callFactoryCompare() &&
                validateEagerlyCompare() &&
                callAdapterFactoryCompare() &&
                converterFactoryCompare();
    }

    public boolean isSameInterceptors() {
        return interceptorsCompare();
    }

    public boolean isSameHostUrl() {
        return hostUrlCompare();
    }
}
