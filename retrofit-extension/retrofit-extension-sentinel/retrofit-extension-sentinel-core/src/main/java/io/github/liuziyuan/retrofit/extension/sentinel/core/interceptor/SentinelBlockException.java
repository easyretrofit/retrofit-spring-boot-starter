package io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor;


import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import okhttp3.Request;

public class SentinelBlockException extends RetrofitExtensionException {

    public SentinelBlockException(String message, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(message, retrofitApiServiceBean, request);
    }

    public SentinelBlockException(Throwable throwable, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {

        super(throwable, retrofitApiServiceBean, request);
    }

    public SentinelBlockException(String message, Throwable throwable, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(message, throwable, retrofitApiServiceBean, request);
    }

}
