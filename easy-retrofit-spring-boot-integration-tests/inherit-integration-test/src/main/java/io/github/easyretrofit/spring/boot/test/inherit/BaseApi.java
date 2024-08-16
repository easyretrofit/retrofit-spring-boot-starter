package io.github.easyretrofit.spring.boot.test.inherit;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8000")
public interface BaseApi {
}
