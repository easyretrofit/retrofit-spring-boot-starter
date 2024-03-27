package io.github.liuziyuan.test.retrofit.spring.boot.inherit;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(globalOverwriteRule = OverrideRule.LOCAL_ONLY, baseUrl = "http://localhost:9000")
public interface OtherApi {
}
