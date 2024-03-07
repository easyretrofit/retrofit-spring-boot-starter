package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Core中RetrofitResourceScanner接口的实现
 * 目的是自定义的Annotation被扫描到
 */
public class SpringBootRetrofitResourceScanner extends RetrofitResourceScanner {
    @Override
    public Set<Class<?>> customRetrofitResourceClasses(Reflections reflections) {
        return null;
    }
}
