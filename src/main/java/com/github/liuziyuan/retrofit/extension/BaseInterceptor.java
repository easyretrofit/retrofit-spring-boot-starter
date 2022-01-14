package com.github.liuziyuan.retrofit.extension;

import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.internal.EverythingIsNonNull;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:37 PM
 */
public abstract class BaseInterceptor implements Interceptor {

    @Override
    @EverythingIsNonNull
    public final Response intercept(Chain chain) {
        return executeIntercept(chain);
    }

    /**
     * execute intercept for OKHttpClient Interceptor
     *
     * @param chain Chain
     * @return Response
     */
    protected abstract Response executeIntercept(Chain chain);
}
