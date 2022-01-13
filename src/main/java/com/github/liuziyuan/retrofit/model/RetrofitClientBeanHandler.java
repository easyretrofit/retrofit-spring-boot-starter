package com.github.liuziyuan.retrofit.model;

import retrofit2.Retrofit;

import java.util.List;

/**
 * @author liuziyuan
 * @date 1/6/2022 3:53 PM
 */
public class RetrofitClientBeanHandler implements Handler<RetrofitClientBean> {
    private List<RetrofitClientBean> clientBeanList;
    private RetrofitServiceBean serviceBean;

    public RetrofitClientBeanHandler(List<RetrofitClientBean> clientBeanList, RetrofitServiceBean serviceBean) {
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
            return clientBean;
        } else {

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

        return false;
    }

    private boolean isSameInterceptors(RetrofitClientBean clientBean, RetrofitServiceBean serviceBean) {
        return false;
    }


}
