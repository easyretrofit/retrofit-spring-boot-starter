package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.annotation.RetrofitBase;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor3;

/**
 * @author liuziyuan
 */
//@RetrofitBase(baseApi = AnnotationApi.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor3.class)
public interface AnnotationApi2 extends AnnotationApi{
}
