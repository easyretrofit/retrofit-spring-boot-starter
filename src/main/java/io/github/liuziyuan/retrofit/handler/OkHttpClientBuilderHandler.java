package io.github.liuziyuan.retrofit.handler;

import io.github.liuziyuan.retrofit.Handler;
import io.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

/**
 * @author liuziyuan
 */
public class OkHttpClientBuilderHandler implements Handler<OkHttpClient.Builder> {
    private final Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilder;

    public OkHttpClientBuilderHandler(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
    }

    @SneakyThrows
    @Override
    public OkHttpClient.Builder generate() {
        if (okHttpClientBuilder.getName().equals(BaseOkHttpClientBuilder.class.getName())) {
            return new OkHttpClient.Builder();
        }
        return this.okHttpClientBuilder.newInstance().executeBuild();
    }
}
