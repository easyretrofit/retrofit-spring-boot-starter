package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.demo.GsonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutorBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:04 AM
 */
class ConverterFactoryGeneratorTest {
    private Class<GsonConverterFactoryBuilder> gsonConverterFactoryBuilderClass;
    private ConverterFactoryGenerator converterFactoryGenerator;

    @BeforeEach
    void setUp() {
        gsonConverterFactoryBuilderClass = GsonConverterFactoryBuilder.class;
    }

    @Test
    void generate() {
        converterFactoryGenerator = new ConverterFactoryGenerator(gsonConverterFactoryBuilderClass, null);
        final Converter.Factory factory = converterFactoryGenerator.generate();
        Assert.assertEquals(factory.getClass().getSimpleName(), GsonConverterFactory.class.getSimpleName());
    }
}