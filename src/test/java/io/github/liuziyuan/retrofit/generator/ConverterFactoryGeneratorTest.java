package io.github.liuziyuan.retrofit.generator;

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
    private Class<? extends Converter.Factory> converterFactoryClass;
    private ConverterFactoryGenerator converterFactoryGenerator;

    @BeforeEach
    void setUp() {
        converterFactoryClass = GsonConverterFactory.class;
    }

    @Test
    void generate() {
        converterFactoryGenerator = new ConverterFactoryGenerator(converterFactoryClass);
        final Converter.Factory factory = converterFactoryGenerator.generate();
        Assert.assertEquals(factory.getClass().getSimpleName(), converterFactoryClass.getSimpleName());
    }
}