package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutor;
import lombok.SneakyThrows;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class CallBackExecutorGenerator implements Generator<Executor> {
    private final Class<? extends BaseCallBackExecutor> callBackExecutorClazz;
    private final Executor executor;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutor> callBackExecutorClazz, Executor executor) {
        this.callBackExecutorClazz = callBackExecutorClazz;
        this.executor = executor;
    }

    @SneakyThrows
    @Override
    public Executor generate() {
        if (executor != null) {
            return executor;
        }
        if (callBackExecutorClazz != null) {
            final String baseCallBackExecutorClazzName = BaseCallBackExecutor.class.getName();
            final String callBackExecutorClazzName = callBackExecutorClazz.getName();
            if (baseCallBackExecutorClazzName.equals(callBackExecutorClazzName)) {
                return null;
            } else {
                return callBackExecutorClazz.newInstance();
            }
        }
        return null;
    }
}
