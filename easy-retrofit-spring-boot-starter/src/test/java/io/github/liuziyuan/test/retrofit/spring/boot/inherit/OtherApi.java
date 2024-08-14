package io.github.liuziyuan.test.retrofit.spring.boot.inherit;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(globalOverwriteRule = OverrideRule.LOCAL_ONLY, baseUrl = "http://localhost:9000")
public interface OtherApi {
}
