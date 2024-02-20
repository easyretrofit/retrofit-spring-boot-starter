package io.github.liuziyuan.retrofit.injectdemo.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.injectdemo.*;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:11 AM
 */
@RetrofitBuilder(baseUrl = "${app.test.base-url}",
        addConverterFactory = {InjectGsonConverterFactoryBuilder.class, InjectJacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = {InjectRxJavaCallAdapterFactoryBuilder.class},
        callbackExecutor = InjectMyCallBackExecutorBuilder.class,
        client = InjectMyOkHttpClient.class)
@RetrofitInterceptor(handler = InjectMyRetrofitInterceptor1.class)
public interface InjectTestApi {
    @GET("/v1/test1/")
    Call<String> test1();
}
