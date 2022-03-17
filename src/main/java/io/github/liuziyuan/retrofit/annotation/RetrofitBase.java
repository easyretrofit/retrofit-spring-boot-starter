package io.github.liuziyuan.retrofit.annotation;

import java.lang.annotation.*;

/**
 * Set the interface that has declared @RetrofitBuilder to replace the interface inheritance method
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitBase {

    /**
     * The class of baseApi is an interface that has declared @RetrofitBuilder
     * @return Class<?>
     */
    Class<?> baseApi();
}
