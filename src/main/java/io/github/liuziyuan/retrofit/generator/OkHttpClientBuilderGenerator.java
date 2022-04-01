package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * Generate OkHttpClientBuilder instance
 *
 * @author liuziyuan
 */
public class OkHttpClientBuilderGenerator implements Generator<OkHttpClient.Builder> {
    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;
    private ApplicationContext applicationContext;

    public OkHttpClientBuilderGenerator(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz, ApplicationContext applicationContext) {
        this.okHttpClientBuilderClazz = okHttpClientBuilderClazz;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public OkHttpClient.Builder generate() {
        try {
            final BaseOkHttpClientBuilder baseOkHttpClientBuilder = applicationContext.getBean(okHttpClientBuilderClazz);
            return baseOkHttpClientBuilder.build();
        } catch (NoSuchBeanDefinitionException ex) {
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
