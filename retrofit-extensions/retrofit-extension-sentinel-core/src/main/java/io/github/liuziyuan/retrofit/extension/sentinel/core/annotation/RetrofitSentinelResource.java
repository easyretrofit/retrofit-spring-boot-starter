package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.extension.sentinel.core.BaseFallBack;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.RetrofitSentinelInterceptor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RetrofitInterceptor(handler = RetrofitSentinelInterceptor.class)
public @interface RetrofitSentinelResource {

    boolean customResourceName() default false;

    int resourceType() default ResourceTypeConstants.COMMON_WEB;

    EntryType entryType() default EntryType.OUT;

    Class<? extends BaseFallBack> fallback() default BaseFallBack.class;
}
