package io.github.easyretrofit.spring.boot.test.inherit;

import io.github.easyretrofit.core.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:41 PM
 */
public class MyRetrofitInterceptor1 extends BaseInterceptor {

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
