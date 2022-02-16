package io.github.liuziyuan.retrofit.handler;

import io.github.liuziyuan.retrofit.Handler;
import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutor;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class CallBackExecutorHandler implements Handler<Executor> {
    private Class<? extends BaseCallBackExecutor> callBackExecutorClazz;
    private Executor executor;

    public CallBackExecutorHandler(Class<? extends BaseCallBackExecutor> callBackExecutorClazz, Executor executor) {
        this.callBackExecutorClazz = callBackExecutorClazz;
        this.executor = executor;
    }

    @Override
    public Executor generate() {
        return null;
    }
}
