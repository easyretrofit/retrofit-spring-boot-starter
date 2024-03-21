package io.github.liuziyuan.test.retrofit.spring.boot.single.instance;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8080", globalOverwriteRule = OverrideRule.LOCAL_ONLY,
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = RxJavaCallAdapterFactoryBuilder.class)
public interface TwoApi {
}
