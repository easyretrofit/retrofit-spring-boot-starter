package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
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

    public Set<Class<?>> scanRetrofitComponentClasses(@Nullable String interfaceSampleName) {
        Set<Class<?>> retrofitResources = this.getRetrofitResource(RetrofitComponent.class);
        Set<Class<?>> results = new HashSet<>();
        for (Class<?> retrofitResource : retrofitResources) {
            if (Arrays.stream(retrofitResource.getInterfaces()).anyMatch(c -> interfaceSampleName.equalsIgnoreCase(c.getName()))) {
                results.add(retrofitResource);
            }
        }
        return results;
    }

    public <T> T getRetrofitComponentGlobalParamConfigInstance(){
        Set<Class<?>> retrofitResources = this.getRetrofitResource(RetrofitComponent.class);
        for (Class<?> retrofitResource : retrofitResources) {
            if (Arrays.stream(retrofitResource.getInterfaces()).anyMatch(c -> "GlobalParamConfig".equalsIgnoreCase(c.getSimpleName()))) {
                try {
                    return (T) retrofitResource.newInstance();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
