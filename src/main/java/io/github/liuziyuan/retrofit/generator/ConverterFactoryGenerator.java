package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseConverterFactoryBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import retrofit2.Converter;

/**
 * Generate ConverterFactory instance
 *
 * @author liuziyuan
 */
public class ConverterFactoryGenerator implements Generator<Converter.Factory> {
    private final Class<? extends BaseConverterFactoryBuilder> baseConverterFactoryBuilderClazz;
    private ApplicationContext applicationContext;

    public ConverterFactoryGenerator(Class<? extends BaseConverterFactoryBuilder> converterFactoryBuilderClazz, ApplicationContext applicationContext) {
        this.baseConverterFactoryBuilderClazz = converterFactoryBuilderClazz;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public Converter.Factory generate() {
        try {
            final BaseConverterFactoryBuilder baseConverterFactoryBuilder = applicationContext.getBean(baseConverterFactoryBuilderClazz);
            return baseConverterFactoryBuilder.executeBuild();
        } catch (NoSuchBeanDefinitionException ex) {
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
