package com.github.liuziyuan.retrofit.extension;

import okhttp3.Response;

import java.io.IOException;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:37 PM
 */
public abstract class BaseInterceptor implements Interceptor {

    @Override
    public final Response intercept(Chain chain) throws IOException {
        return executeIntercept(chain);
    }

    protected abstract Response executeIntercept(Chain chain);
}
