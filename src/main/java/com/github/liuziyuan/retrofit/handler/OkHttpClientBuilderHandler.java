package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.Handler;
import com.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:16 AM
 */
public class OkHttpClientBuilderHandler implements Handler<OkHttpClient.Builder> {
    private final Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilder;

    public OkHttpClientBuilderHandler(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
    }

    @SneakyThrows
    @Override
    public OkHttpClient.Builder generate() {
        return this.okHttpClientBuilder.newInstance().executeBuild();
    }
}
