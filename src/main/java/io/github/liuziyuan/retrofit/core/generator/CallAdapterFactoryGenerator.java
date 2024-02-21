package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.AppContext;
import io.github.liuziyuan.retrofit.core.Generator;
import io.github.liuziyuan.retrofit.core.extension.BaseCallAdapterFactoryBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import retrofit2.CallAdapter;

/**
 * Generate CallAdapterFactory instance
 *
 * @author liuziyuan
 */
public class CallAdapterFactoryGenerator implements Generator<CallAdapter.Factory> {
    private final Class<? extends BaseCallAdapterFactoryBuilder> baseCallAdapterFactoryBuilderClazz;
    private final AppContext applicationContext;

    public CallAdapterFactoryGenerator(Class<? extends BaseCallAdapterFactoryBuilder> baseCallAdapterFactoryBuilderClazz, AppContext applicationContext) {
        this.baseCallAdapterFactoryBuilderClazz = baseCallAdapterFactoryBuilderClazz;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public CallAdapter.Factory generate() {
        final BaseCallAdapterFactoryBuilder baseCallAdapterFactoryBuilder = applicationContext.getBean(baseCallAdapterFactoryBuilderClazz);
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
