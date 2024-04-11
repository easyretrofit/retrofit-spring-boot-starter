package io.github.liuziyuan.retrofit.spring.boot.reactor;

import io.github.liuziyuan.retrofit.adapter.reactor.ReactorCallAdapterFactory;
import io.github.liuziyuan.retrofit.core.builder.BaseCallAdapterFactoryBuilder;
import retrofit2.CallAdapter;

public class ReactorCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return ReactorCallAdapterFactory.create();
    }
}
