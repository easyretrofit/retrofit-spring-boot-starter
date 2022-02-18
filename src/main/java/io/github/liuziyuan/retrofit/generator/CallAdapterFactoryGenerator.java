package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseCallAdapterFactoryBuilder;
import lombok.SneakyThrows;
import retrofit2.CallAdapter;

/**
 * Generate CallAdapterFactory instance
 *
 * @author liuziyuan
 */
public class CallAdapterFactoryGenerator implements Generator<CallAdapter.Factory> {
    private final Class<? extends BaseCallAdapterFactoryBuilder> baseCallAdapterFactoryBuilderClazz;
    private final CallAdapter.Factory factory;

    public CallAdapterFactoryGenerator(Class<? extends BaseCallAdapterFactoryBuilder> baseCallAdapterFactoryBuilderClazz, CallAdapter.Factory factory) {
        this.baseCallAdapterFactoryBuilderClazz = baseCallAdapterFactoryBuilderClazz;
        this.factory = factory;
    }

    @SneakyThrows
    @Override
    public CallAdapter.Factory generate() {
        if (factory != null) {
            return factory;
        }
        if (baseCallAdapterFactoryBuilderClazz != null) {
            final String baseCallAdapterFactoryBuilderClazzName = BaseCallAdapterFactoryBuilder.class.getName();
            final String callAdapterFactoryBuilderClazzName = baseCallAdapterFactoryBuilderClazz.getName();
            if (baseCallAdapterFactoryBuilderClazzName.equals(callAdapterFactoryBuilderClazzName)) {
                return null;
            } else {
                final BaseCallAdapterFactoryBuilder builder = baseCallAdapterFactoryBuilderClazz.newInstance();
                return builder.executeBuild();
            }
        }
        return null;
    }
}
