package io.github.easyretrofit.spring.boot.it.inherit;

import io.github.easyretrofit.core.annotation.RetrofitInterceptor;

@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface ComplexL3Api extends ComplexL2Api{
}
