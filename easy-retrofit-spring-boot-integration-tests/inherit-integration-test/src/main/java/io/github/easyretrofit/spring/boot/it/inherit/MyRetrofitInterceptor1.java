package io.github.easyretrofit.spring.boot.it.inherit;

import io.github.easyretrofit.core.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liuziyuan
 */
public class MyRetrofitInterceptor1 extends BaseInterceptor {

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
