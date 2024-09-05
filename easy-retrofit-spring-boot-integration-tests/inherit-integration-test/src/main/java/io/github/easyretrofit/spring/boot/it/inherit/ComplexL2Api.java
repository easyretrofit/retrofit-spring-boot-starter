package io.github.easyretrofit.spring.boot.it.inherit;

import io.github.easyretrofit.core.annotation.RetrofitInterceptor;

@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
public interface ComplexL2Api extends ComplexApi{
}
