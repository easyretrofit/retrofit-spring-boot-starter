package io.github.liuziyuan.retrofit.generator;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liuziyuan
 * @date 1/14/2022 10:53 AM
 */
class CallAdapterFactoryGeneratorTest {

    private Class<? extends CallAdapter.Factory> callAdapterFactoryClass;
    private CallAdapterFactoryGenerator callAdapterFactoryGenerator;

    @BeforeEach
    void setUp() {
        callAdapterFactoryClass = RxJavaCallAdapterFactory.class;
    }

    @Test
    void generate() {
        callAdapterFactoryGenerator = new CallAdapterFactoryGenerator(callAdapterFactoryClass);
        final CallAdapter.Factory factory = callAdapterFactoryGenerator.generate();
        Assert.assertEquals(factory.getClass().getSimpleName(), callAdapterFactoryClass.getSimpleName());
    }
}