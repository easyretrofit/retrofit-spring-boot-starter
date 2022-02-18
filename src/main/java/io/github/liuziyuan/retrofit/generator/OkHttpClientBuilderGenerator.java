package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

/**
 * Generate OkHttpClientBuilder instance
 *
 * @author liuziyuan
 */
public class OkHttpClientBuilderGenerator implements Generator<OkHttpClient.Builder> {
    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;
    private OkHttpClient.Builder componentOkHttpClientBuilder;

    public OkHttpClientBuilderGenerator(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz, OkHttpClient.Builder componentOkHttpClientBuilder) {
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
            final String okHttpClientBuilderClazzName = okHttpClientBuilderClazz.getName();
            final String baseOkHttpClientBuilderClazzName = BaseOkHttpClientBuilder.class.getName();
            if (okHttpClientBuilderClazzName.equals(baseOkHttpClientBuilderClazzName)) {
                return new OkHttpClient.Builder();
            } else {
                return this.okHttpClientBuilderClazz.newInstance().executeBuild();
            }
        }
        return null;
    }
}
