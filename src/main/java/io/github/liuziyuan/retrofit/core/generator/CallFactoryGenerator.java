package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.AppContext;
import io.github.liuziyuan.retrofit.core.Generator;
import io.github.liuziyuan.retrofit.core.extension.BaseCallBackExecutorBuilder;
import io.github.liuziyuan.retrofit.core.extension.BaseCallFactoryBuilder;
import lombok.SneakyThrows;
import okhttp3.Call;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * @author liuziyuan
 */
public abstract class CallFactoryGenerator implements Generator<Call.Factory> {
    private final Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;

    public CallFactoryGenerator(Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz) {
        this.callFactoryBuilderClazz = callFactoryBuilderClazz;
    }

    public abstract BaseCallFactoryBuilder buildInjectionObject(Class<? extends BaseCallFactoryBuilder> clazz);

    @SneakyThrows
    @Override
    public Call.Factory generate() {
        BaseCallFactoryBuilder baseCallFactoryBuilder = buildInjectionObject(callFactoryBuilderClazz);
        if (baseCallFactoryBuilder != null) {
            return baseCallFactoryBuilder.executeBuild();
        }
        if (callFactoryBuilderClazz != null) {
            final String baseCallFactoryBuilderClazzName = BaseCallFactoryBuilder.class.getName();
            final String callFactoryBuilderClazzName = callFactoryBuilderClazz.getName();
            if (baseCallFactoryBuilderClazzName.equals(callFactoryBuilderClazzName)) {
                return null;
            } else {
                final BaseCallFactoryBuilder builder = callFactoryBuilderClazz.newInstance();
                return builder.executeBuild();
            }
        }
        return null;
    }
}
