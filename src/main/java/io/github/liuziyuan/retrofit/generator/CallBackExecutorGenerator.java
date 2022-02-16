package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutor;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class CallBackExecutorGenerator implements Generator<Executor> {
    private Class<? extends BaseCallBackExecutor> callBackExecutorClazz;
    private Executor executor;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutor> callBackExecutorClazz, Executor executor) {
        this.callBackExecutorClazz = callBackExecutorClazz;
        this.executor = executor;
    }

    @Override
    public Executor generate() {
        return null;
    }
}
