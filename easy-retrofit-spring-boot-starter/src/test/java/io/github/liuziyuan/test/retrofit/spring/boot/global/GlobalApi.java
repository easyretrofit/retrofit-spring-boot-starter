package io.github.liuziyuan.test.retrofit.spring.boot.global;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.test.retrofit.spring.boot.common.Rx2JavaCallAdapterFactoryBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8000", globalOverwriteRule = OverrideRule.GLOBAL_ONLY, addCallAdapterFactory = Rx2JavaCallAdapterFactoryBuilder.class)
public interface GlobalApi {
}
