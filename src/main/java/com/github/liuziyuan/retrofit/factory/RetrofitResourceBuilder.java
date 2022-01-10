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
    private Set<Class<?>> retrofitBuilderClassSet;
    private Environment environment;

    public RetrofitResourceBuilder(Set<Class<?>> retrofitBuilderClassSet, Environment environment) {
        retrofitClientBeanList = new ArrayList<>();
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
        this.environment = environment;
    }

    public List<RetrofitClientBean> build() {
        setRetrofitClientBeanList();
        setRetrofitServiceBeanListToClient();
        return retrofitClientBeanList;
    }

    private void setRetrofitClientBeanList() {
        RetrofitClientBeanHandler clientBeanHandler;
        for (Class<?> retrofitBuilderClass : retrofitBuilderClassSet) {
            clientBeanHandler = new RetrofitClientBeanHandler(retrofitBuilderClass, environment);
            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
            if (retrofitClientBean != null) {
                retrofitClientBeanList.add(retrofitClientBean);
            }
        }
    }

    private void setRetrofitServiceBeanListToClient() {
        RetrofitServiceBeanHandler serviceBeanHandler;
        for (Class<?> retrofitBuilderClass : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitServiceBeanHandler(retrofitBuilderClass);
            RetrofitServiceBean retrofitServiceBean = serviceBeanHandler.generate();
            if (retrofitServiceBean != null) {
                for (RetrofitClientBean retrofitClientBean : retrofitClientBeanList) {
                    if (retrofitServiceBean.getParentClazz().getSimpleName().equals(retrofitClientBean.getSelfClazz().getSimpleName())) {
                        retrofitClientBean.addRetrofitServiceBean(retrofitServiceBean);
                        break;
                    }
                }
            }
        }
    }


}
