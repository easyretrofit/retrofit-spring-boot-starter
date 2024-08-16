package io.github.easyretrofit.spring.boot.test.single.api;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.spring.boot.test.single.controller.HelloService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Invocation;

import java.util.Objects;

/**
 *
 * @author liuziyuan
 * @date 1/5/2022 5:44 PM
 */
@Slf4j
@Component
public class MyRetrofitInterceptor extends BaseInterceptor {

    @Autowired
    private RetrofitResourceContext context;

    @Autowired
    private HelloService helloService;


    @Value("${okhttpclient.timeout}")
    private int timeout;

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        String clazzName = Objects.requireNonNull(request.tag(Invocation.class)).method().getDeclaringClass().getName();
        final RetrofitApiInterfaceBean currentServiceBean = context.getRetrofitApiServiceBean(clazzName);
        log.info("HelloService: {}", helloService.hello("retrofit"));
        log.info("time out: {}", timeout);
        // TODO if you need
        return chain.proceed(request);
    }
}
