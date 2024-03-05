package io.github.liuziyuan.retrofit.core.exception;

import io.github.liuziyuan.retrofit.core.exception.RetrofitStarterException;

/**
 * @author liuziyuan
 */
public class BaseUrlException extends RetrofitStarterException {

    public BaseUrlException(String message) {
        super(message);
    }

    public BaseUrlException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
