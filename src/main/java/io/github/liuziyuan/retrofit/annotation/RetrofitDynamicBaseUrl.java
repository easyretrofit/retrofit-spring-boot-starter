package io.github.liuziyuan.retrofit.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * The value of @RetrofitDynamicBaseUrl can dynamically replace the baseurl of @RetrofitBuilder
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitDynamicBaseUrl {
    String value() default StringUtils.EMPTY;
}
