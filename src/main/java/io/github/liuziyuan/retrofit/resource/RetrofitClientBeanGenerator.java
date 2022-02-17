package io.github.liuziyuan.retrofit.resource;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseConverterFactoryBuilder;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * generate RetrofitClientBean object
 *
 * @author liuziyuan
 */
public class RetrofitClientBeanGenerator implements Generator<RetrofitClientBean> {
    private final List<RetrofitClientBean> clientBeanList;
    private final RetrofitServiceBean serviceBean;

    public RetrofitClientBeanGenerator(List<RetrofitClientBean> clientBeanList, RetrofitServiceBean serviceBean) {
        this.clientBeanList = clientBeanList;
        this.serviceBean = serviceBean;
    }

    @Override
    public RetrofitClientBean generate() {
        RetrofitClientBean clientBean = findExistRetrofitClientBean(serviceBean, clientBeanList);
        if (clientBean == null) {
            clientBean = new RetrofitClientBean();
            clientBean.setRetrofitBuilder(serviceBean.getRetrofitBuilder());
            clientBean.setInterceptors(serviceBean.getInterceptors());
            clientBean.setRealHostUrl(serviceBean.getRetrofitUrl().getRealHostUrl());
            clientBean.setRetrofitInstanceName(Retrofit.class.getSimpleName());
        }
        clientBean.addRetrofitServiceBean(serviceBean);
        return clientBean;
    }

    private RetrofitClientBean findExistRetrofitClientBean(RetrofitServiceBean serviceBean, List<RetrofitClientBean> clientBeanList) {
        for (RetrofitClientBean clientBean : clientBeanList) {
            if (isSameHostUrl(clientBean, serviceBean) &&
                    isSameRetrofitBuilder(clientBean, serviceBean) &&
                    isSameInterceptors(clientBean, serviceBean)) {
                return clientBean;
            }

        }
        return null;
    }


    private boolean isSameHostUrl(RetrofitClientBean clientBean, RetrofitServiceBean serviceBean) {
        return serviceBean.getRetrofitUrl().getRealHostUrl().equals(clientBean.getRealHostUrl());
    }

    private boolean isSameRetrofitBuilder(RetrofitClientBean clientBean, RetrofitServiceBean serviceBean) {
        final String clientBeanRetrofitBuilderOkHttpClientSimpleName = clientBean.getRetrofitBuilder().client().getSimpleName();
        List<String> clientBeanCallAdapterFactoryList = new ArrayList<>();
        for (Class<? extends CallAdapter.Factory> clazz : clientBean.getRetrofitBuilder().addCallAdapterFactory()) {
            clientBeanCallAdapterFactoryList.add(clazz.getSimpleName());
        }
        List<String> clientBeanConverterFactoryList = new ArrayList<>();
        for (Class<? extends BaseConverterFactoryBuilder> clazz : clientBean.getRetrofitBuilder().addConverterFactory()) {
            clientBeanConverterFactoryList.add(clazz.getSimpleName());
        }

        final String serviceBeanRetrofitBuilderOkHttpClientSimpleName = serviceBean.getRetrofitBuilder().client().getSimpleName();
        List<String> serviceBeanCallAdapterFactoryList = new ArrayList<>();
        for (Class<? extends CallAdapter.Factory> clazz : serviceBean.getRetrofitBuilder().addCallAdapterFactory()) {
            serviceBeanCallAdapterFactoryList.add(clazz.getSimpleName());
        }
        List<String> serviceBeanConverterFactoryList = new ArrayList<>();
        for (Class<? extends BaseConverterFactoryBuilder> clazz : serviceBean.getRetrofitBuilder().addConverterFactory()) {
            serviceBeanConverterFactoryList.add(clazz.getSimpleName());
        }

        return clientBeanRetrofitBuilderOkHttpClientSimpleName.equals(serviceBeanRetrofitBuilderOkHttpClientSimpleName) &&
                clientBeanCallAdapterFactoryList.containsAll(serviceBeanCallAdapterFactoryList) && serviceBeanCallAdapterFactoryList.containsAll(clientBeanCallAdapterFactoryList) &&
                clientBeanConverterFactoryList.containsAll(serviceBeanConverterFactoryList) && serviceBeanConverterFactoryList.containsAll(clientBeanConverterFactoryList);
    }

    private boolean isSameInterceptors(RetrofitClientBean clientBean, RetrofitServiceBean serviceBean) {
        List<String> clientBeanInterceptorSimpleNameList = new ArrayList<>();
        clientBean.getInterceptors().forEach(i -> clientBeanInterceptorSimpleNameList.add(i.handler().getSimpleName()));
        List<String> serviceBeanInterceptorSimpleNameList = new ArrayList<>();
        serviceBean.getInterceptors().forEach(i -> serviceBeanInterceptorSimpleNameList.add(i.handler().getSimpleName()));
        return clientBeanInterceptorSimpleNameList.containsAll(serviceBeanInterceptorSimpleNameList) &&
                serviceBeanInterceptorSimpleNameList.containsAll(clientBeanInterceptorSimpleNameList);
    }

}
