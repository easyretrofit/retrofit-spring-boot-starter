package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBeanHandler;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBeanHandler;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author liuziyuan
 */
public class RetrofitResourceBuilder {

    private List<RetrofitClientBean> retrofitClientBeanList;
    private final Environment environment;

    public RetrofitResourceBuilder(Environment environment) {
        retrofitClientBeanList = new ArrayList<>();
        this.environment = environment;
    }

    public void build(Set<Class<?>> retrofitBuilderClassSet) {
        List<RetrofitServiceBean> retrofitServiceBeanList = setRetrofitServiceBeanList(retrofitBuilderClassSet);
        retrofitClientBeanList = setRetrofitClientBeanList(retrofitServiceBeanList);
    }

    public List<RetrofitClientBean> getRetrofitClientBeanList() {
        return retrofitClientBeanList;
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
        RetrofitClientBeanHandler clientBeanHandler;
        for (RetrofitServiceBean serviceBean : serviceBeanList) {
            clientBeanHandler = new RetrofitClientBeanHandler(clientBeanList, serviceBean);
            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
            if (retrofitClientBean != null && clientBeanList.stream().noneMatch(clientBean -> clientBean.getRetrofitInstanceName().equals(retrofitClientBean.getRetrofitInstanceName()))) {
                clientBeanList.add(retrofitClientBean);
            }
        }
        return clientBeanList;
    }


}
