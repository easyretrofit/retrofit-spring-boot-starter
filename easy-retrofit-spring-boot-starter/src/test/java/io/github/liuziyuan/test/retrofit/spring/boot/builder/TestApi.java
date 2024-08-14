package io.github.liuziyuan.test.retrofit.spring.boot.builder;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;

@RetrofitBuilder(baseUrl = "http://192.168.100.1:9000",
        addCallAdapterFactory = RxJavaCallAdapterFactoryBuilder.class,
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        callbackExecutor = MyCallBackExecutorBuilder.class,
        client = MyOkHttpClient.class,
        validateEagerly = false)
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor3.class)
public interface TestApi {
}
