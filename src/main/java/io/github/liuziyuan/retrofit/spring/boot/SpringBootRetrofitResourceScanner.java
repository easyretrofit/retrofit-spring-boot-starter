package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import org.reflections.Reflections;

import java.util.Set;

public class SpringBootRetrofitResourceScanner extends RetrofitResourceScanner {
    @Override
    public Set<Class<?>> customRetrofitResourceClasses(Reflections reflections) {
        return null;
    }
}
