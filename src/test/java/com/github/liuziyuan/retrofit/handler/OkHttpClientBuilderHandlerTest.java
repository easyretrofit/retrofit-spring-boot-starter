package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.demo.MyOkHttpClient;
import com.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:31 AM
 */
class OkHttpClientBuilderHandlerTest {

    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilder;
    private OkHttpClientBuilderHandler okHttpClientBuilderHandler;

    @BeforeEach
    void setUp() {
        okHttpClientBuilder = MyOkHttpClient.class;
    }

    @Test
    void generate() {
        okHttpClientBuilderHandler = new OkHttpClientBuilderHandler(okHttpClientBuilder);
        final OkHttpClient.Builder generate = okHttpClientBuilderHandler.generate();
        Assert.assertNotNull(generate.build());
    }
}