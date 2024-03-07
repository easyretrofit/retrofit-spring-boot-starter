package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.injectdemo.*;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:11 AM
 */
@RetrofitBuilder(baseUrl = "http://192.168.100.1:9000")
@RetrofitCloudService(name = "abc")
public interface CloudTestApi {
    @GET("/v1/test1/")
    Call<String> test1();
}
