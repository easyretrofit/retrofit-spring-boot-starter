package io.github.liuziyuan.retrofit.handler;

import io.github.liuziyuan.retrofit.Handler;
import io.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

/**
 * Generate OkHttpClientBuilder instance
 * @author liuziyuan
 */
public class OkHttpClientBuilderHandler implements Handler<OkHttpClient.Builder> {
    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;
    private OkHttpClient.Builder componentOkHttpClientBuilder;

    public OkHttpClientBuilderHandler(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz, OkHttpClient.Builder componentOkHttpClientBuilder) {
        this.okHttpClientBuilderClazz = okHttpClientBuilderClazz;
        this.componentOkHttpClientBuilder = componentOkHttpClientBuilder;
    }

    @SneakyThrows
    @Override
    public OkHttpClient.Builder generate() {
        if (componentOkHttpClientBuilder != null) {
            return componentOkHttpClientBuilder;
        }
        if (okHttpClientBuilderClazz != null) {
            if (okHttpClientBuilderClazz.getName().equals(BaseOkHttpClientBuilder.class.getName())) {
                return new OkHttpClient.Builder();
            } else {
                return this.okHttpClientBuilderClazz.newInstance().executeBuild();
            }
        }
        return null;
    }
}
