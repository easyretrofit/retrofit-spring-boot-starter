package io.github.liuziyuan.test.retrofit.spring.boot.common;

import io.github.liuziyuan.retrofit.core.builder.BaseCallAdapterFactoryBuilder;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author liuziyuan
 */
public class Rx2JavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }
}
