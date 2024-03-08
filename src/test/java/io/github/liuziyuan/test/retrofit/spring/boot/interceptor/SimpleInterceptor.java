package io.github.liuziyuan.test.retrofit.spring.boot.interceptor;

import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Response;

public class SimpleInterceptor extends BaseInterceptor {
    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        return chain.proceed(chain.request());
    }
}
