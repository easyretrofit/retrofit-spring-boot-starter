package com.github.liuziyuan.retrofit.annotation;

import com.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import retrofit2.CallAdapter;
import retrofit2.Converter;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 * @date 12/31/2021 9:42 AM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitBuilder {
    String baseUrl() default "";

    Class<? extends CallAdapter.Factory>[] addCallAdapterFactory() default {};

    Class<? extends Converter.Factory>[] addConverterFactory() default {};

    Class<? extends BaseOkHttpClientBuilder> client() default BaseOkHttpClientBuilder.class;

}
