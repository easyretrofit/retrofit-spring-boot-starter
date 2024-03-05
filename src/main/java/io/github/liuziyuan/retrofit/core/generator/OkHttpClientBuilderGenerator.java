package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.builder.BaseOkHttpClientBuilder;
import io.github.liuziyuan.retrofit.core.generator.Generator;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

/**
 * Generate OkHttpClientBuilder instance
 *
 * @author liuziyuan
 */
public abstract class OkHttpClientBuilderGenerator implements Generator<OkHttpClient.Builder> {
    private final Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;

    public OkHttpClientBuilderGenerator(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz) {
        this.okHttpClientBuilderClazz = okHttpClientBuilderClazz;
    }

    public abstract BaseOkHttpClientBuilder buildInjectionObject(Class<? extends BaseOkHttpClientBuilder> clazz);

    @SneakyThrows
    @Override
    public OkHttpClient.Builder generate() {

        final BaseOkHttpClientBuilder baseOkHttpClientBuilder = buildInjectionObject(okHttpClientBuilderClazz);
        if (baseOkHttpClientBuilder != null) {
            return baseOkHttpClientBuilder.executeBuild();
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
