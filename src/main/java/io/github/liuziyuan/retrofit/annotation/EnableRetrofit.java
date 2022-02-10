package io.github.liuziyuan.retrofit.annotation;

import io.github.liuziyuan.retrofit.RetrofitResourceImportDefinitionRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({RetrofitResourceImportDefinitionRegistry.class})
public @interface EnableRetrofit {

    /**
     * Scan base package paths of retrofit resources
     * Same as basePackages method
     *
     * @return basePackages
     */
    String[] value() default {};

    /**
     * Scan base package paths of retrofit resources
     *
     * @return basePackages
     */
    String[] basePackages() default {};

    /**
     * Scan base package paths by classes of retrofit resources
     *
     * @return basePackages
     */
    Class<?>[] basePackageClasses() default {};
}
