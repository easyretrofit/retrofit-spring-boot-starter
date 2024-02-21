package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.core.builder.BaseCallAdapterFactoryBuilder;
import org.springframework.stereotype.Component;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liuziyuan
 */
@Component
public class InjectRxJavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
