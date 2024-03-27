package io.github.liuziyuan.retrofit.core.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * Set the prefix of the URL to reduce the duplicate path of the URL on the method
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitUrlPrefix {
    String value() default StringUtils.EMPTY;
}
