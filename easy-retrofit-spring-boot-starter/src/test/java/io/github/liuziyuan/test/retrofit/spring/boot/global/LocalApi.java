package io.github.liuziyuan.test.retrofit.spring.boot.global;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.test.retrofit.spring.boot.common.Rx3JavaCallAdapterFactoryBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8004", globalOverwriteRule = OverrideRule.LOCAL_ONLY, addCallAdapterFactory = Rx3JavaCallAdapterFactoryBuilder.class)
public interface LocalApi {
}
