package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.Generator;
import io.github.liuziyuan.retrofit.core.extension.BaseCallFactoryBuilder;
import lombok.SneakyThrows;
import okhttp3.Call;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * @author liuziyuan
 */
public class CallFactoryGenerator implements Generator<Call.Factory> {
    private final Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;
    private final ApplicationContext applicationContext;

    public CallFactoryGenerator(Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz, ApplicationContext applicationContext) {
        this.callFactoryBuilderClazz = callFactoryBuilderClazz;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public Call.Factory generate() {
        try {
            BaseCallFactoryBuilder baseCallFactoryBuilder = applicationContext.getBean(callFactoryBuilderClazz);
            return baseCallFactoryBuilder.executeBuild();
        } catch (NoSuchBeanDefinitionException ignored) {
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
