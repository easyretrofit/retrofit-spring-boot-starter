package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor1;
import com.github.liuziyuan.retrofit.extension.BaseInterceptor;
import okhttp3.Interceptor;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:49 AM
 */
class OkHttpInterceptorHandlerTest {

    private Class<? extends BaseInterceptor> interceptorClass;
    private OkHttpInterceptorHandler okHttpInterceptorHandler;

    @BeforeEach
    void setUp() {
        interceptorClass = MyRetrofitInterceptor1.class;
    }

    @Test
    void generate() {
        okHttpInterceptorHandler = new OkHttpInterceptorHandler(interceptorClass);
        final Interceptor generate = okHttpInterceptorHandler.generate();
        Assert.assertNotNull(generate);
    }
}