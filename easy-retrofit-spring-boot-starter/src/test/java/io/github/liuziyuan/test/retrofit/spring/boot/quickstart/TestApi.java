package io.github.liuziyuan.test.retrofit.spring.boot.quickstart;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.http.GET;

@RetrofitBuilder(baseUrl = "${app.test.base-url}", globalOverwriteRule = OverrideRule.LOCAL_ONLY)
public interface TestApi {

    @GET("hello/message")
    Call<String> getMessage();
}
