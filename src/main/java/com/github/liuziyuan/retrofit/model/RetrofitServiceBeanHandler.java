package com.github.liuziyuan.retrofit.model;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;

/**
 * @author liuziyuan
 * @date 1/6/2022 3:54 PM
 */
public class RetrofitServiceBeanHandler implements Handler<RetrofitServiceBean> {
    private Class<?> clazz;

    public RetrofitServiceBeanHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public RetrofitServiceBean generate() {
        RetrofitServiceBean retrofitServiceBean = new RetrofitServiceBean();
        retrofitServiceBean.setSelfClazz(clazz);
        retrofitServiceBean.setParentClazz(findParentRetrofitBuilderClazz(clazz));
        return retrofitServiceBean;
    }

    private Class<?> findParentRetrofitBuilderClazz(Class<?> clazz) {
        RetrofitBuilder retrofitBuilder = clazz.getAnnotation(RetrofitBuilder.class);
        Class<?> targetClazz = clazz;
        if (retrofitBuilder == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                targetClazz = findParentRetrofitBuilderClazz(interfaces[0]);
            }
        }
        return targetClazz;
    }


}
