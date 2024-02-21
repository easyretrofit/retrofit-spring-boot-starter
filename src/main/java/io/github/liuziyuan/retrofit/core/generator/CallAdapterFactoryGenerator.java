package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.extension.BaseCallAdapterFactoryBuilder;
import lombok.SneakyThrows;
import retrofit2.CallAdapter;

/**
 * Generate CallAdapterFactory instance
 *
 * @author liuziyuan
 */
public abstract class CallAdapterFactoryGenerator implements Generator<CallAdapter.Factory> {
    private final Class<? extends BaseCallAdapterFactoryBuilder> baseCallAdapterFactoryBuilderClazz;

    public CallAdapterFactoryGenerator(Class<? extends BaseCallAdapterFactoryBuilder> baseCallAdapterFactoryBuilderClazz) {
        this.baseCallAdapterFactoryBuilderClazz = baseCallAdapterFactoryBuilderClazz;
    }

    public abstract BaseCallAdapterFactoryBuilder buildInjectionObject(Class<? extends BaseCallAdapterFactoryBuilder> clazz);

    @SneakyThrows
    @Override
    public CallAdapter.Factory generate() {
        final BaseCallAdapterFactoryBuilder baseCallAdapterFactoryBuilder = buildInjectionObject(baseCallAdapterFactoryBuilderClazz);
        if (baseCallAdapterFactoryBuilder != null) {
            return baseCallAdapterFactoryBuilder.executeBuild();
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
