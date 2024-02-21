package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.AppContext;
import io.github.liuziyuan.retrofit.core.Generator;
import io.github.liuziyuan.retrofit.core.extension.BaseCallBackExecutorBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class CallBackExecutorGenerator implements Generator<Executor> {
    private final Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;
    private final AppContext applicationContext;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz, AppContext applicationContext) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public Executor generate() {

        final BaseCallBackExecutorBuilder baseCallBackExecutorBuilder = applicationContext.getBean(callBackExecutorBuilderClazz);
        if (baseCallBackExecutorBuilder != null) {
            return baseCallBackExecutorBuilder.executeBuild();
        }
        if (callBackExecutorBuilderClazz != null) {
            final String baseCallBackExecutorClazzName = BaseCallBackExecutorBuilder.class.getName();
            final String callBackExecutorClazzName = callBackExecutorBuilderClazz.getName();
            if (baseCallBackExecutorClazzName.equals(callBackExecutorClazzName)) {
                return null;
            } else {
                final BaseCallBackExecutorBuilder builder = callBackExecutorBuilderClazz.newInstance();
                return builder.executeBuild();
            }
        }
        return null;
    }
}
