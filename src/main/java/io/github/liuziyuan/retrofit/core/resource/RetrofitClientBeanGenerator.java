package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.Generator;
import retrofit2.Retrofit;

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
            clientBean.setRealHostUrl(serviceBean.getRetrofitUrl().getDefaultUrl().getRealHostUrl());
            clientBean.setRetrofitInstanceName(Retrofit.class.getSimpleName());
            clientBean.setUrlStatus(serviceBean.getRetrofitUrl().getUrlStatus());
        }
        clientBean.addInheritedInterceptors(serviceBean.getMyInterceptors());
        clientBean.addRetrofitServiceBean(serviceBean);
        return clientBean;
    }

    private RetrofitClientBean findExistRetrofitClientBean(RetrofitServiceBean serviceBean, List<RetrofitClientBean> clientBeanList) {
        for (RetrofitClientBean clientBean : clientBeanList) {
            RetrofitResourceComparer comparer = new RetrofitResourceComparer(clientBean, serviceBean);
            if (comparer.isSameHostUrl() &&
                    comparer.isDummyUrlCompare() &&
                    comparer.isSameRetrofitBuilderInstance() &&
                    comparer.isSameInterceptors()) {
                return clientBean;
            }
        }
        return null;
    }


}
