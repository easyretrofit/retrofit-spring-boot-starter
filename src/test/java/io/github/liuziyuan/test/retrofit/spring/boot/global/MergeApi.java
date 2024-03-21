package io.github.liuziyuan.test.retrofit.spring.boot.global;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.test.retrofit.spring.boot.common.Rx3JavaCallAdapterFactoryBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8002", globalOverwriteRule = OverrideRule.MERGE, addCallAdapterFactory = Rx3JavaCallAdapterFactoryBuilder.class)
public interface MergeApi {
}
