package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.extension.BaseCallBackExecutorBuilder;
import lombok.SneakyThrows;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public abstract class CallBackExecutorGenerator implements Generator<Executor> {
    private final Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
    }

    public abstract BaseCallBackExecutorBuilder buildInjectionObject(Class<? extends BaseCallBackExecutorBuilder> clazz);

    @SneakyThrows
    @Override
    public Executor generate() {

        final BaseCallBackExecutorBuilder baseCallBackExecutorBuilder = buildInjectionObject(callBackExecutorBuilderClazz);
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
