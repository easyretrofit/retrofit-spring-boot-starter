package io.github.liuziyuan.test.retrofit.spring.boot.extension;


import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import org.springframework.stereotype.Component;

public class MyException extends RetrofitExtensionException {

    public MyException(String message) {
        super(message, null, null);
    }
}
