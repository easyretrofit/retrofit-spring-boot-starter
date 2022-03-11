package io.github.liuziyuan.retrofit.annotation;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitBase {
    Class<?> baseApi();
}
