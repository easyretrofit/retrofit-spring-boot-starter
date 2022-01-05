package com.github.liuziyuan.retrofit.factory;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liuziyuan
 * @date 1/5/2022 11:10 AM
 */
public class RetrofitResourceScanner {

    private Environment environment;
    private ResourceLoader resourceLoader;
    private Set<Class<?>> typesAnnotatedWithRetrofitBuilder;
    private Set<Class<?>> typesAnnotatedWithRetrofitInterceptor;


    public void doScan(String... basePackages) {
        Pattern filterPattern = Pattern.compile(Arrays.stream(basePackages)
                .map(s -> s.replace(".", "/"))
                .collect(Collectors.joining("|", "(", ").*?")));
        ConfigurationBuilder configuration = new ConfigurationBuilder().forPackages(basePackages).filterInputsBy(s -> filterPattern.matcher(s).matches());
        Reflections reflections = new Reflections(configuration);
        typesAnnotatedWithRetrofitBuilder = reflections.getTypesAnnotatedWith(RetrofitBuilder.class);
        typesAnnotatedWithRetrofitInterceptor = reflections.getTypesAnnotatedWith(RetrofitInterceptor.class);
        return;
    }

}
