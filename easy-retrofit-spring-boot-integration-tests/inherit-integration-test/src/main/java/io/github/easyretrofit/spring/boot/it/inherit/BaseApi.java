package io.github.easyretrofit.spring.boot.it.inherit;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitFallBack;

@RetrofitFallBack(BaseApiFallBack.class)
@RetrofitBuilder(baseUrl = "http://localhost:8000")
public interface BaseApi {
}
