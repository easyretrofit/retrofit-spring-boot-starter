package io.github.liuziyuan.test.retrofit.spring.boot.builder;

import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
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
