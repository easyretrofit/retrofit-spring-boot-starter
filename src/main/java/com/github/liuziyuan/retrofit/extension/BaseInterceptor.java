package com.github.liuziyuan.retrofit.extension;

import com.github.liuziyuan.retrofit.RetrofitResourceContext;
import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.internal.EverythingIsNonNull;

/**
 * @author liuziyuan
 */
public abstract class BaseInterceptor implements Interceptor {

    protected RetrofitResourceContext context;

    public BaseInterceptor(RetrofitResourceContext context) {
        this.context = context;
    }

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
