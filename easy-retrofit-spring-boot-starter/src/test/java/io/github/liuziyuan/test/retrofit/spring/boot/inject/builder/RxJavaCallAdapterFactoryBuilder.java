package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.builder.BaseCallAdapterFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liuziyuan
 */
@Component
public class RxJavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {

    @Autowired
    private RetrofitResourceContext retrofitResourceContext;
    @Autowired
    private ApplicationContext context;
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
