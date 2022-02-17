package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.demo.GsonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.demo.JacksonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor1;
import io.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor2;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:11 AM
 */
@RetrofitBuilder(baseUrl = "${app.test.base-url}",
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactory.class})
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface TestApi {
}
