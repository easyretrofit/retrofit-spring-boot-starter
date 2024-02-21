package io.github.liuziyuan.retrofit.demo;

import io.github.liuziyuan.retrofit.core.builder.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:41 PM
 */

public class MyRetrofitInterceptor3 extends BaseInterceptor {

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
