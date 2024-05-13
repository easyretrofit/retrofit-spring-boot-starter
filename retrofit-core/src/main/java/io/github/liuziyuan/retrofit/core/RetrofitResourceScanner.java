package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBase;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Scan retrofit API resources using @RetrofitBuilder
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitResourceScanner {

    public Set<Class<?>> doScan(String... basePackages) {
        Reflections reflections = getReflections(basePackages);
        final Set<Class<?>> retrofitBuilderClasses = getRetrofitResourceClasses(reflections, RetrofitBuilder.class);
        final Set<Class<?>> retrofitBaseApiClasses = getRetrofitResourceClasses(reflections, RetrofitBase.class);
        retrofitBuilderClasses.addAll(retrofitBaseApiClasses);
        return retrofitBuilderClasses;
    }

    public RetrofitExtension doScanExtension(String... basePackages) {
        Reflections reflections = getReflections(basePackages);
        RetrofitExtension retrofitExtension = new RetrofitExtension();
        Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderClasses = reflections.getSubTypesOf(RetrofitBuilderExtension.class);
        Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorClasses = reflections.getSubTypesOf(RetrofitInterceptorExtension.class);
        retrofitExtension.setRetrofitBuilderClasses(retrofitBuilderClasses);
        retrofitExtension.setRetrofitInterceptorClasses(retrofitInterceptorClasses);
        return retrofitExtension;
    }

    public Set<Class<?>> getRetrofitResource(Class<? extends Annotation> clazz, String... basePackages) {
        Reflections reflections = getReflections(basePackages);
        return reflections.getTypesAnnotatedWith(clazz);
    }

    public Set<Class<?>> getRetrofitResourceClasses(Reflections reflections, Class<? extends Annotation> annotationClass) {
        final Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(annotationClass);
        Iterator<Class<?>> iterator = classSet.iterator();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            if (!clazz.isInterface()) {
                // 使用iterator的remove方法安全地移除元素
                iterator.remove();
                log.warn("[{}] requires an interface type", clazz.getName());
            }
        }
        return classSet;
    }

    private Reflections getReflections(String[] basePackages) {
        ConfigurationBuilder configuration;
        if (basePackages.length == 0) {
            configuration = new ConfigurationBuilder().forPackages("");
        } else {
            Pattern filterPattern = Pattern.compile(Arrays.stream(basePackages)
                    .map(s -> s.replace(".", "/"))
                    .collect(Collectors.joining("|", ".*?(", ").*?")));
            log.debug("Scanner Pattern : {}", filterPattern.pattern());
            configuration = new ConfigurationBuilder().forPackages(basePackages).filterInputsBy(s ->
            {
                log.debug("Filter inputs {}", s);
                return filterPattern.matcher(s).matches();
            });

        }
        return new Reflections(configuration);
    }

    @Getter
    @Setter
    public static class RetrofitExtension {
        Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderClasses;
        Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorClasses;

        public RetrofitExtension() {
            retrofitInterceptorClasses = new HashSet<>();
            retrofitBuilderClasses = new HashSet<>();
        }

    }

}
