package io.github.liuziyuan.test.retrofit.spring.boot.global;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.test.retrofit.spring.boot.common.Rx3JavaCallAdapterFactoryBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8003", globalOverwriteRule = OverrideRule.LOCAL_FIRST, addCallAdapterFactory = Rx3JavaCallAdapterFactoryBuilder.class)
public interface LocalFirstApi {
}
