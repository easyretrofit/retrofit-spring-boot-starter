package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class RetrofitTestInterceptor extends BaseInterceptor {
    @Override
    protected Response executeIntercept(Chain chain) {
        return null;
    }
}
