package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.demo.*;

/**
 * @author liuziyuan
 * @date 1/10/2022 2:10 PM
 */

@RetrofitBuilder(baseUrl = "${app.hello-inherit.base-url}",
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactoryBuilder.class},
        client = MyOkHttpClient.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface HelloInheritApi {
}
