package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.RetrofitResourceContext;
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
    private RetrofitResourceContext context;

    @BeforeEach
    void setUp() {
        interceptorClass = MyRetrofitInterceptor1.class;
        context = new RetrofitResourceContext();
    }

    @Test
    void generate() {
        okHttpInterceptorHandler = new OkHttpInterceptorHandler(interceptorClass, context);
        final Interceptor generate = okHttpInterceptorHandler.generate();
        Assert.assertNotNull(generate);
    }
}