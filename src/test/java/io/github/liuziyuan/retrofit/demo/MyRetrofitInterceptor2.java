package io.github.liuziyuan.retrofit.demo;

import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:44 PM
 */
public class MyRetrofitInterceptor2 extends BaseInterceptor {

    public MyRetrofitInterceptor2(RetrofitResourceContext context) {
        super(context);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
