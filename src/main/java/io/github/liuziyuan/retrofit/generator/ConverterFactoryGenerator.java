package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import lombok.SneakyThrows;
import retrofit2.Converter;

import java.lang.reflect.Method;

/**
 * Generate ConverterFactory instance
 * @author liuziyuan
 */
public class ConverterFactoryGenerator implements Generator<Converter.Factory> {
    private final Class<? extends Converter.Factory> converterFactoryClass;

    public ConverterFactoryGenerator(Class<? extends Converter.Factory> converterFactoryClass) {
        this.converterFactoryClass = converterFactoryClass;
    }

    @SneakyThrows
    @Override
    public Converter.Factory generate() {
        final Method createMethod = converterFactoryClass.getDeclaredMethod("create");
        final Object invoke = createMethod.invoke(null);
        return (Converter.Factory) invoke;
    }
}
