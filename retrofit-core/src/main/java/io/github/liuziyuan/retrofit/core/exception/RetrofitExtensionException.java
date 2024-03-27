package io.github.liuziyuan.retrofit.core.exception;

import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import lombok.Getter;
import okhttp3.Request;

@Getter
public class RetrofitExtensionException extends RuntimeException {

    protected final RetrofitApiServiceBean retrofitApiServiceBean;

    protected final Request request;

    public RetrofitExtensionException(String message, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(message);
        this.retrofitApiServiceBean = retrofitApiServiceBean;
        this.request = request;
    }

    public RetrofitExtensionException(String message, Throwable cause, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(message, cause);
        this.retrofitApiServiceBean = retrofitApiServiceBean;
        this.request = request;
    }

    public RetrofitExtensionException(Throwable cause, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(cause);
        this.retrofitApiServiceBean = retrofitApiServiceBean;
        this.request = request;
    }
}
