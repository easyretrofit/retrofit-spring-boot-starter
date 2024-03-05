package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.generator.Generator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitResourceComparer;
import retrofit2.Retrofit;

import java.util.List;

/**
 * generate RetrofitClientBean object
 *
 * @author liuziyuan
 */
public class RetrofitClientBeanGenerator implements Generator<io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean> {
    private final List<io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean> clientBeanList;
    private final io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean serviceBean;

    public RetrofitClientBeanGenerator(List<io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean> clientBeanList, io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean serviceBean) {
        this.clientBeanList = clientBeanList;
        this.serviceBean = serviceBean;
    }

    @Override
    public io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean generate() {
        io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean clientBean = findExistRetrofitClientBean(serviceBean, clientBeanList);
        if (clientBean == null) {
            clientBean = new io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean();
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

    private io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean findExistRetrofitClientBean(RetrofitApiServiceBean serviceBean, List<io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean> clientBeanList) {
        for (RetrofitClientBean clientBean : clientBeanList) {
            io.github.liuziyuan.retrofit.core.resource.RetrofitResourceComparer comparer = new RetrofitResourceComparer(clientBean, serviceBean);
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
