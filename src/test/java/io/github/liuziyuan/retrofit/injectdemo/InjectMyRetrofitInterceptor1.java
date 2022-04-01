package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:41 PM
 */
@Component
public class InjectMyRetrofitInterceptor1 extends BaseInterceptor {

    @Autowired
    private RetrofitResourceContext context;

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
