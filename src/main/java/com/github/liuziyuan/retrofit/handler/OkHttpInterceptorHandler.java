package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.extension.BaseInterceptor;
import okhttp3.Interceptor;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:43 AM
 */
public class OkHttpInterceptorHandler implements Handler<Interceptor> {
    private Class<? extends BaseInterceptor> interceptorClass;

    public OkHttpInterceptorHandler(Class<? extends BaseInterceptor> interceptorClass) {
        this.interceptorClass = interceptorClass;
    }

    @Override
    public Interceptor generate() {
        try {
            final BaseInterceptor interceptor = interceptorClass.newInstance();
            return interceptor;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
