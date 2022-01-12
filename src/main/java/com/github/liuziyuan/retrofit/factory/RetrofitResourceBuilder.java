package com.github.liuziyuan.retrofit.factory;

import com.github.liuziyuan.retrofit.model.RetrofitClientBean;
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

    public RetrofitResourceBuilder(Environment environment) {
        retrofitClientBeanList = new ArrayList<>();
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
        this.environment = environment;
    }

    public List<RetrofitClientBean> build(Set<Class<?>> retrofitBuilderClassSet) {
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
//        setRetrofitClientBeanList();
//        setRetrofitServiceBeanListToClient();
        List<RetrofitServiceBean> retrofitServiceBeanList = setRetrofitServiceBeanList();


        return retrofitClientBeanList;
    }

    private List<RetrofitServiceBean> setRetrofitServiceBeanList() {
        List<RetrofitServiceBean> retrofitServiceBeanList = new ArrayList<>();
        RetrofitServiceBeanHandler serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitServiceBeanHandler(clazz, environment);
            final RetrofitServiceBean serviceBean = serviceBeanHandler.generate();
            retrofitServiceBeanList.add(serviceBean);
        }
        return retrofitServiceBeanList;
    }

//    private void setRetrofitClientBeanList() {
//        RetrofitClientBeanHandler clientBeanHandler;
//        for (Class<?> retrofitBuilderClass : retrofitBuilderClassSet) {
//            clientBeanHandler = new RetrofitClientBeanHandler(retrofitBuilderClass, environment);
//            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
//            if (retrofitClientBean != null) {
//                retrofitClientBeanList.add(retrofitClientBean);
//            }
//        }
//    }
//
//    private void setRetrofitServiceBeanListToClient() {
//        RetrofitServiceBeanHandler serviceBeanHandler;
//        for (Class<?> retrofitBuilderClass : retrofitBuilderClassSet) {
//            serviceBeanHandler = new RetrofitServiceBeanHandler(retrofitBuilderClass);
//            RetrofitServiceBean retrofitServiceBean = serviceBeanHandler.generate();
//            if (retrofitServiceBean != null) {
//                for (RetrofitClientBean retrofitClientBean : retrofitClientBeanList) {
//                    if (retrofitServiceBean.getParentClazz().getSimpleName().equals(retrofitClientBean.getSelfClazz().getSimpleName())) {
//                        retrofitClientBean.addRetrofitServiceBean(retrofitServiceBean);
//                        break;
//                    }
//                }
//            }
//        }
//    }


}
