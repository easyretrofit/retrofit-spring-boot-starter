package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutorBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class CallBackExecutorGenerator implements Generator<Executor> {
    private final Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;
    private ApplicationContext applicationContext;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz, ApplicationContext applicationContext) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public Executor generate() {
        try {
            final BaseCallBackExecutorBuilder baseCallBackExecutorBuilder = applicationContext.getBean(callBackExecutorBuilderClazz);
            Executor executor = baseCallBackExecutorBuilder.executeBuild();
            return executor;
        } catch (NoSuchBeanDefinitionException ex) {

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
