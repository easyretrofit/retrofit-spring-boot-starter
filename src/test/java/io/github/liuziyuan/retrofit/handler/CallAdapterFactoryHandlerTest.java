package io.github.liuziyuan.retrofit.handler;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liuziyuan
 * @date 1/14/2022 10:53 AM
 */
class CallAdapterFactoryHandlerTest {

    private Class<? extends CallAdapter.Factory> callAdapterFactoryClass;
    private CallAdapterFactoryHandler callAdapterFactoryHandler;

    @BeforeEach
    void setUp() {
        callAdapterFactoryClass = RxJavaCallAdapterFactory.class;
    }

    @Test
    void generate() {
        callAdapterFactoryHandler = new CallAdapterFactoryHandler(callAdapterFactoryClass);
        final CallAdapter.Factory factory = callAdapterFactoryHandler.generate();
        Assert.assertEquals(factory.getClass().getSimpleName(), callAdapterFactoryClass.getSimpleName());
    }
}