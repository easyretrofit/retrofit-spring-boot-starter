package io.github.easyretrofit.spring.boot.test.single.api;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.spring.boot.test.single.domain.HelloBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * <p><b>Base URLs should always end in {@code /}.</b>
 * <p><b>Endpoint values which contain a leading {@code /} are absolute.</b>
 * <p><b>{@code ${app.hello.url}} need set in application.yml</b>
 * <p><b>Except baseUrl, other attributes are not required</b>
 *
 * @author liuziyuan
 */
@RetrofitBuilder(baseUrl = "${app.hello.v1.url}",
        addConverterFactory = {GsonConvertFactoryBuilder.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactoryBuilder.class})
@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)
public interface HelloApi {
    /**
     * call hello API method of backend service
     *
     * @param message message
     * @return hello bean
     */
    @GET("backend/v1/hello/{message}")
    Call<HelloBean> hello(@Path("message") String message);
}
