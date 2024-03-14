package io.github.liuziyuan.test.retrofit.spring.boot.quickstart;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.http.GET;

@RetrofitBuilder(baseUrl = "${app.test.base-url}", denyGlobalConfig = true)
public interface TestApi {

    @GET("hello/message")
    Call<String> getMessage();
}
