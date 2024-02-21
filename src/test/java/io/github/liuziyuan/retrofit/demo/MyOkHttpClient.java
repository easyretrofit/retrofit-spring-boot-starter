package io.github.liuziyuan.retrofit.demo;

import io.github.liuziyuan.retrofit.core.builder.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;

import java.time.Duration;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:54 AM
 */
public class MyOkHttpClient extends BaseOkHttpClientBuilder {

    private int timeout = 3000;

    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder.connectTimeout(Duration.ofMillis(timeout));
    }
}
