package io.github.liuziyuan.retrofit.springboot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import org.reflections.Reflections;

import java.util.Set;

public class SpringBootRetrofitResourceScanner extends RetrofitResourceScanner {
    @Override
    public Set<Class<?>> customRetrofitResourceClasses(Reflections reflections) {
        return null;
    }
}
