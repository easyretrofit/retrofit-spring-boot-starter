package com.github.liuziyuan.retrofit.handler;

import retrofit2.CallAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author liuziyuan
 * @date 1/14/2022 10:33 AM
 */
public class CallAdapterFactoryHandler implements Handler<CallAdapter.Factory> {
    private Class<? extends CallAdapter.Factory> callAdapterFactoryClass;

    public CallAdapterFactoryHandler(Class<? extends CallAdapter.Factory> callAdapterFactoryClass) {
        this.callAdapterFactoryClass = callAdapterFactoryClass;
    }

    @Override
    public CallAdapter.Factory generate() {
        try {
            final Method createMethod = callAdapterFactoryClass.getDeclaredMethod("create");
            final Object invoke = createMethod.invoke(null);
            return (CallAdapter.Factory) invoke;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
