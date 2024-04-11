package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.extension.sentinel.core.BaseFallBack;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.RetrofitSentinelInterceptor;

import java.lang.annotation.*;

/**
 * Declare to enable RetrofitSentinelResource in the interface
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RetrofitInterceptor(handler = RetrofitSentinelInterceptor.class)
public @interface RetrofitSentinelResource {

    /**
     * the default value is Sentinel official resource type
     *
     * @return resource type
     */
    int resourceType() default ResourceTypeConstants.COMMON_WEB;

    /**
     * the default value is Sentinel official entry type
     *
     * @return entry type
     */
    EntryType entryType() default EntryType.OUT;


    /**
     * if you want to use fallback, you must implement BaseFallBack. <br>
     * if not set fallback, this resource will not be use fallback
     *
     * @return a class that implements BaseFallBack
     */
    Class<? extends BaseFallBack> fallback() default BaseFallBack.class;
}
