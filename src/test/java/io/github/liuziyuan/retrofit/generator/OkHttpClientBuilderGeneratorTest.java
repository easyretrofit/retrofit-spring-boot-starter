package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.demo.MyOkHttpClient;
import io.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:31 AM
 */
class OkHttpClientBuilderGeneratorTest {

    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilder;
    private OkHttpClientBuilderGenerator okHttpClientBuilderGenerator;

    @BeforeEach
    void setUp() {
        okHttpClientBuilder = MyOkHttpClient.class;
    }

    @Test
    void generate() {
        okHttpClientBuilderGenerator = new OkHttpClientBuilderGenerator(okHttpClientBuilder, null);
        final OkHttpClient.Builder generate = okHttpClientBuilderGenerator.generate();
        Assert.assertNotNull(generate.build());
    }
}