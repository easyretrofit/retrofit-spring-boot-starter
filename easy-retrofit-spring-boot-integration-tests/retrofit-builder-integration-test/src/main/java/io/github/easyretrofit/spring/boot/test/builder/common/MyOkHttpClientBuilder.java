package io.github.easyretrofit.spring.boot.test.builder.common;

import io.github.easyretrofit.core.builder.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;

import java.time.Duration;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:54 AM
 */
public class MyOkHttpClientBuilder extends BaseOkHttpClientBuilder {

    private int timeout = 3000;

    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder.connectTimeout(Duration.ofMillis(timeout));
    }
}
