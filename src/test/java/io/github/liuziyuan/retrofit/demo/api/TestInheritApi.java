package io.github.liuziyuan.retrofit.demo.api;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author liuziyuan
 * @date 1/5/2022 4:57 PM
 */

public interface TestInheritApi extends TestApi {
    @GET("/v1/test1/")
    Call<String> test1();
}
