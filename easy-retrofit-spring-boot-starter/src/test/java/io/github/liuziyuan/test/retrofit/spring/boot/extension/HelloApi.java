package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.annotation.RetrofitInterceptorParam;

@RetrofitBuilder()
@RetrofitTestAnno(extension = @RetrofitInterceptorParam(sort = 10))
@RetrofitInterceptor(handler = RetrofitTestInterceptor.class, sort = 10)
public interface HelloApi {
}
