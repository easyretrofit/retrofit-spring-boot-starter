package io.github.liuziyuan.retrofit.exception;

/**
 * @author liuziyuan
 */
public class RetrofitSpringBootStarterException extends RuntimeException {
    public RetrofitSpringBootStarterException(String message) {
        super(message);
    }

    public RetrofitSpringBootStarterException(String message, Throwable cause) {
        super(message, cause);
    }
}
