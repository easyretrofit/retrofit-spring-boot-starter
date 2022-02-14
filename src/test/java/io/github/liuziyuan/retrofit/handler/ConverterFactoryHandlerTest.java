package io.github.liuziyuan.retrofit.handler;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:04 AM
 */
class ConverterFactoryHandlerTest {
    private Class<? extends Converter.Factory> converterFactoryClass;
    private ConverterFactoryHandler converterFactoryHandler;

    @BeforeEach
    void setUp() {
        converterFactoryClass = GsonConverterFactory.class;
    }

    @Test
    void generate() {
        converterFactoryHandler = new ConverterFactoryHandler(converterFactoryClass);
        final Converter.Factory factory = converterFactoryHandler.generate();
        Assert.assertEquals(factory.getClass().getSimpleName(), converterFactoryClass.getSimpleName());
    }
}