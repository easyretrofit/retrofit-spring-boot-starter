package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseConverterFactoryBuilder;
import lombok.SneakyThrows;
import retrofit2.Converter;

/**
 * Generate ConverterFactory instance
 *
 * @author liuziyuan
 */
public class ConverterFactoryGenerator implements Generator<Converter.Factory> {
    private final Class<? extends BaseConverterFactoryBuilder> baseConverterFactoryBuilderClazz;
    private final Converter.Factory factory;

    public ConverterFactoryGenerator(Class<? extends BaseConverterFactoryBuilder> baseConverterFactoryBuilderClazz, Converter.Factory factory) {
        this.baseConverterFactoryBuilderClazz = baseConverterFactoryBuilderClazz;
        this.factory = factory;
    }

    @SneakyThrows
    @Override
    public Converter.Factory generate() {
        if (factory != null) {
            return factory;
        }
        if (baseConverterFactoryBuilderClazz != null) {
            final String baseConverterFactoryBuilderClazzName = BaseConverterFactoryBuilder.class.getName();
            final String converterFactoryBuilderClazzName = baseConverterFactoryBuilderClazz.getName();
            if (baseConverterFactoryBuilderClazzName.equals(converterFactoryBuilderClazzName)) {
                return null;
            } else {
                final BaseConverterFactoryBuilder baseConverterFactoryBuilder = baseConverterFactoryBuilderClazz.newInstance();
                return baseConverterFactoryBuilder.executeBuild();
            }
        }
        return null;
    }
}
