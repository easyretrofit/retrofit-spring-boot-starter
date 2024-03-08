package io.github.liuziyuan.test.retrofit.spring.boot.common;

import io.github.liuziyuan.retrofit.core.builder.BaseCallAdapterFactoryBuilder;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liuziyuan
 */
public class RxJavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
