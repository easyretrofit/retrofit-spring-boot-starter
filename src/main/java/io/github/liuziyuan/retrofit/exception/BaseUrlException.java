package io.github.liuziyuan.retrofit.exception;

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
