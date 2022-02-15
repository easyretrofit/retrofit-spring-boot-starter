package io.github.liuziyuan.retrofit.handler;

import io.github.liuziyuan.retrofit.Handler;
import lombok.SneakyThrows;
import retrofit2.CallAdapter;

import java.lang.reflect.Method;

/**
 * Generate CallAdapterFactory instance
 * @author liuziyuan
 */
public class CallAdapterFactoryHandler implements Handler<CallAdapter.Factory> {
    private final Class<? extends CallAdapter.Factory> callAdapterFactoryClass;

    public CallAdapterFactoryHandler(Class<? extends CallAdapter.Factory> callAdapterFactoryClass) {
        this.callAdapterFactoryClass = callAdapterFactoryClass;
    }

    @SneakyThrows
    @Override
    public CallAdapter.Factory generate() {
        final Method createMethod = callAdapterFactoryClass.getDeclaredMethod("create");
        final Object invoke = createMethod.invoke(null);
        return (CallAdapter.Factory) invoke;
    }
}
