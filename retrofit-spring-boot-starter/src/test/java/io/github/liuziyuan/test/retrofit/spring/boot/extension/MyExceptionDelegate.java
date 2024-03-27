package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.proxy.ExceptionDelegate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

public class MyExceptionDelegate extends BaseExceptionDelegate<MyException> {


    public MyExceptionDelegate(Class<MyException> exceptionClassName) {
        super(exceptionClassName);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, RetrofitExtensionException throwable) {
        return null;
    }
}
