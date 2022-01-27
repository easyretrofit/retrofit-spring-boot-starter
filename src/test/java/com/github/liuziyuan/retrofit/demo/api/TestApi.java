package com.github.liuziyuan.retrofit.demo.api;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import com.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor1;
import com.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor2;
import retrofit2.Call;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:11 AM
 */
@RetrofitBuilder(baseUrl = "${app.test.base-url}",
        addConverterFactory = {GsonConverterFactory.class, JacksonConverterFactory.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactory.class})
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface TestApi {
}
