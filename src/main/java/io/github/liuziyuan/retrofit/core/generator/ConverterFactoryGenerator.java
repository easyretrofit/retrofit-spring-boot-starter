package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.builder.BaseConverterFactoryBuilder;
import lombok.SneakyThrows;
import retrofit2.Converter;

/**
 * Generate ConverterFactory instance
 *
 * @author liuziyuan
 */
public abstract class ConverterFactoryGenerator implements Generator<Converter.Factory> {
    private final Class<? extends BaseConverterFactoryBuilder> baseConverterFactoryBuilderClazz;

    public ConverterFactoryGenerator(Class<? extends BaseConverterFactoryBuilder> converterFactoryBuilderClazz) {
        this.baseConverterFactoryBuilderClazz = converterFactoryBuilderClazz;
    }

    public abstract BaseConverterFactoryBuilder buildInjectionObject(Class<? extends BaseConverterFactoryBuilder> clazz);

    @SneakyThrows
    @Override
    public Converter.Factory generate() {

        final BaseConverterFactoryBuilder baseConverterFactoryBuilder = buildInjectionObject(baseConverterFactoryBuilderClazz);
        if (baseConverterFactoryBuilder != null) {
            return baseConverterFactoryBuilder.executeBuild();
        }
        if (baseConverterFactoryBuilderClazz != null) {
            final String baseConverterFactoryBuilderClazzName = BaseConverterFactoryBuilder.class.getName();
            final String converterFactoryBuilderClazzName = baseConverterFactoryBuilderClazz.getName();
            if (baseConverterFactoryBuilderClazzName.equals(converterFactoryBuilderClazzName)) {
                return null;
            } else {
                final BaseConverterFactoryBuilder builder = baseConverterFactoryBuilderClazz.newInstance();
                return builder.executeBuild();
            }
        }
        return null;
    }
}
