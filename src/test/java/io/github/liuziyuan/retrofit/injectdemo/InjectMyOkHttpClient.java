package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.core.builder.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:54 AM
 */
@Component
public class InjectMyOkHttpClient extends BaseOkHttpClientBuilder {

    @Value("${okhttpclient.timeout}")
    private int timeout;

    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder.connectTimeout(Duration.ofMillis(timeout));
    }
}
