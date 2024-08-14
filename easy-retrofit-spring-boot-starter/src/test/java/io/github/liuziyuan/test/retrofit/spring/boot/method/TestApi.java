package io.github.liuziyuan.test.retrofit.spring.boot.method;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.http.GET;

@TestMethod
@RetrofitBuilder(baseUrl = "${app.test.base-url}", globalOverwriteRule = OverrideRule.LOCAL_ONLY)
public interface TestApi {

    @TestMethod
    @TestMethod
    @GET("hello/message")
    Call<String> getMessage();
}
