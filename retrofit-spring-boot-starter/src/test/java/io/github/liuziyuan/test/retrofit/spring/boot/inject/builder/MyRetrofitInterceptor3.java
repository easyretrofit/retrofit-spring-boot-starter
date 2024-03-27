package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:41 PM
 */
@Component
public class MyRetrofitInterceptor3 extends BaseInterceptor {
    @Autowired
    private RetrofitResourceContext retrofitResourceContext;
    @Autowired
    private ApplicationContext context;
    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
