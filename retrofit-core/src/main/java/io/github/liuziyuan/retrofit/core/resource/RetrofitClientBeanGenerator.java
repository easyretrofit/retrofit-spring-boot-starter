package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.generator.Generator;

import java.util.List;

/**
 * generate RetrofitClientBean object
 *
 * @author liuziyuan
 */
public class RetrofitClientBeanGenerator implements Generator<RetrofitClientBean> {
    private final List<RetrofitClientBean> clientBeanList;
    private final RetrofitApiServiceBean serviceBean;

    public RetrofitClientBeanGenerator(List<RetrofitClientBean> clientBeanList, RetrofitApiServiceBean serviceBean) {
        this.clientBeanList = clientBeanList;
        this.serviceBean = serviceBean;
    }

    @Override
    public RetrofitClientBean generate() {
        RetrofitClientBean clientBean = findExistRetrofitClientBean(serviceBean, clientBeanList);
        if (clientBean == null) {
            clientBean = new RetrofitClientBean(serviceBean);
        }
        clientBean.addRetrofitApiServiceBean(serviceBean);
        return clientBean;
    }

    private RetrofitClientBean findExistRetrofitClientBean(RetrofitApiServiceBean serviceBean, List<RetrofitClientBean> clientBeanList) {
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
