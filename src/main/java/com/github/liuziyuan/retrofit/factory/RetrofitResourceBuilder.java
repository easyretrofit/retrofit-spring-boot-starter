package com.github.liuziyuan.retrofit.factory;

import com.github.liuziyuan.retrofit.model.RetrofitClientBean;
import com.github.liuziyuan.retrofit.model.RetrofitClientBeanHandler;
import com.github.liuziyuan.retrofit.model.RetrofitServiceBean;
import com.github.liuziyuan.retrofit.model.RetrofitServiceBeanHandler;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author liuziyuan
 * @date 1/5/2022 11:10 AM
 */
public class RetrofitResourceBuilder {

    private List<RetrofitClientBean> retrofitClientBeanList;
    private Environment environment;

    public RetrofitResourceBuilder(Environment environment) {
        retrofitClientBeanList = new ArrayList<>();
        this.environment = environment;
    }

    public void build(Set<Class<?>> retrofitBuilderClassSet) {
        List<RetrofitServiceBean> retrofitServiceBeanList = setRetrofitServiceBeanList(retrofitBuilderClassSet);
        retrofitClientBeanList = setRetrofitClientBeanList(retrofitServiceBeanList);
    }

    private List<RetrofitServiceBean> setRetrofitServiceBeanList(Set<Class<?>> retrofitBuilderClassSet) {
        List<RetrofitServiceBean> retrofitServiceBeanList = new ArrayList<>();
        RetrofitServiceBeanHandler serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitServiceBeanHandler(clazz, environment);
            final RetrofitServiceBean serviceBean = serviceBeanHandler.generate();
            retrofitServiceBeanList.add(serviceBean);
        }
        return retrofitServiceBeanList;
    }

    private List<RetrofitClientBean> setRetrofitClientBeanList(List<RetrofitServiceBean> serviceBeanList) {
        List<RetrofitClientBean> clientBeanList = new ArrayList<>();
        RetrofitClientBeanHandler retrofitClientBeanHandler;
        for (RetrofitServiceBean serviceBean : serviceBeanList) {
            retrofitClientBeanHandler = new RetrofitClientBeanHandler(clientBeanList, serviceBean);
            final RetrofitClientBean retrofitClientBean = retrofitClientBeanHandler.generate();
            if (retrofitClientBean != null) {
                clientBeanList.add(retrofitClientBean);
            }
        }
        return clientBeanList;
    }


}
