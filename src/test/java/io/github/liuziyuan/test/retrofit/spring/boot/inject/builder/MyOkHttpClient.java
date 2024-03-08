package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.builder.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:54 AM
 */
@Component
public class MyOkHttpClient extends BaseOkHttpClientBuilder {

    @Autowired
    private RetrofitResourceContext retrofitResourceContext;
    @Autowired
    private ApplicationContext context;

    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder;
    }
}
