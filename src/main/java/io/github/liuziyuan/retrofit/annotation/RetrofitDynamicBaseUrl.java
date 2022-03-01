package io.github.liuziyuan.retrofit.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitDynamicBaseUrl {
    String value() default StringUtils.EMPTY;
}
