package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor3;

/**
 * @author liuziyuan
 */
@RetrofitInterceptor(handler = MyRetrofitInterceptor3.class, exclude = "/v1/**")
public interface TestMiddleApi extends TestApi{
}
