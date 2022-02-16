package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import lombok.SneakyThrows;
import retrofit2.CallAdapter;

import java.lang.reflect.Method;

/**
 * Generate CallAdapterFactory instance
 * @author liuziyuan
 */
public class CallAdapterFactoryGenerator implements Generator<CallAdapter.Factory> {
    private final Class<? extends CallAdapter.Factory> callAdapterFactoryClass;

    public CallAdapterFactoryGenerator(Class<? extends CallAdapter.Factory> callAdapterFactoryClass) {
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
