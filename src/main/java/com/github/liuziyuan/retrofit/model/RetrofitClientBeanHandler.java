package com.github.liuziyuan.retrofit.model;

import retrofit2.Retrofit;

import java.util.Set;

/**
 * @author liuziyuan
 * @date 1/6/2022 3:53 PM
 */
public class RetrofitClientBeanHandler implements Handler<RetrofitClientBean> {
    private Set<RetrofitClientBean> clientBeanList;
    private RetrofitServiceBean serviceBean;

    public RetrofitClientBeanHandler(Set<RetrofitClientBean> clientBeanList, RetrofitServiceBean serviceBean) {
        this.clientBeanList = clientBeanList;
        this.serviceBean = serviceBean;
    }

    @Override
    public RetrofitClientBean generate() {
        RetrofitClientBean clientBean;
        if (isNewRetrofitClient(serviceBean, clientBeanList)) {
            clientBean = new RetrofitClientBean();
            clientBean.setRetrofitBuilder(serviceBean.getRetrofitBuilder());
            clientBean.setInterceptors(serviceBean.getInterceptors());
            clientBean.setRealHostUrl(serviceBean.getRetrofitUrl().getRealHostUrl());
            clientBean.setRetrofitInstanceName(Retrofit.class.getSimpleName());
            return clientBean;
        }
        return null;
    }

    private boolean isNewRetrofitClient(RetrofitServiceBean serviceBean, Set<RetrofitClientBean> clientBeanList) {
        if (clientBeanList.isEmpty()) {
            return true;
        }
        for (RetrofitClientBean clientBean : clientBeanList) {
            if (serviceBean.getRetrofitUrl().getRealHostUrl().equals(clientBean.getRealHostUrl())) {
                return true;
            }
        }
        return false;
    }


}
