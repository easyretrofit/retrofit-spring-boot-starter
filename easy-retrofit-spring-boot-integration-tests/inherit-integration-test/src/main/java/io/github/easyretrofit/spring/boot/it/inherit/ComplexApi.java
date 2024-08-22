package io.github.easyretrofit.spring.boot.it.inherit;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;

@RetrofitInterceptor(handler = MyRetrofitInterceptor3.class)
@RetrofitBuilder(baseUrl = "http://localhost:8100")
public interface ComplexApi {
}
