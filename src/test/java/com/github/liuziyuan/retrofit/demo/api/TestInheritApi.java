package com.github.liuziyuan.retrofit.demo.api;

import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import com.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor2;

/**
 * @author liuziyuan
 * @date 1/5/2022 4:57 PM
 */
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface TestInheritApi extends TestApi {
}
