package com.github.liuziyuan.retrofit.demo.api;

import com.github.liuziyuan.retrofit.MyRetrofitInterceptor2;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;

/**
 * @author liuziyuan
 * @date 1/5/2022 4:57 PM
 */
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public class TestInheritApi extends TestApi {
}
