package com.github.liuziyuan.retrofit.model;

import org.springframework.core.env.Environment;

/**
 * @author liuziyuan
 * @date 1/6/2022 3:53 PM
 */
public class RetrofitClientBeanHandler implements Handler<RetrofitClientBean> {
    private Class<?> retrofitBuilderClass;
    private Environment environment;

    public RetrofitClientBeanHandler(Class<?> retrofitBuilderClass, Environment environment) {
        this.retrofitBuilderClass = retrofitBuilderClass;
        this.environment = environment;
    }

    @Override
    public RetrofitClientBean generate() {
        return null;
    }


}
