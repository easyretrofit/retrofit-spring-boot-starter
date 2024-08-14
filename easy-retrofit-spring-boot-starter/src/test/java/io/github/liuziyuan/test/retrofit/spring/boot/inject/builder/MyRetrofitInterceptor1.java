package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import lombok.Getter;
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
@Getter
@Component
public class MyRetrofitInterceptor1 extends BaseInterceptor {
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
