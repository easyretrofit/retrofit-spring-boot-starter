package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.demo.*;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author liuziyuan
 * @date 1/10/2022 2:10 PM
 */

@RetrofitBuilder(baseUrl = "${app.hello.base-url}",
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactoryBuilder.class},
        callbackExecutor = MyCallBackExecutorBuilder.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
public interface HelloApi {
}
