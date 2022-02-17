package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.demo.GsonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.demo.JacksonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.demo.MyCallBackExecutorBuilder;
import io.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor1;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author liuziyuan
 * @date 1/10/2022 2:10 PM
 */

@RetrofitBuilder(baseUrl = "${app.hello.base-url}",
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactory.class},
        callbackExecutor = MyCallBackExecutorBuilder.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
public interface HelloApi {
}
