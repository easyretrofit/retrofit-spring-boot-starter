package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutorBuilder;
import lombok.SneakyThrows;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class CallBackExecutorGenerator implements Generator<Executor> {
    private final Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;
    private final Executor executor;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz, Executor executor) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
        this.executor = executor;
    }

    @SneakyThrows
    @Override
    public Executor generate() {
        if (executor != null) {
            return executor;
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
