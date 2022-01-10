package com.github.liuziyuan.retrofit.factory;

import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liuziyuan
 * @date 1/5/2022 11:10 AM
 */
public class RetrofitResourceScanner {



    public Set<Class<?>> doScan(String... basePackages) {
        ConfigurationBuilder configuration;
        if (basePackages.length == 0) {
            configuration = new ConfigurationBuilder().forPackages("");
        } else {
            Pattern filterPattern = Pattern.compile(Arrays.stream(basePackages)
                    .map(s -> s.replace(".", "/"))
                    .collect(Collectors.joining("|", "(", ").*?")));
            configuration = new ConfigurationBuilder().forPackages(basePackages).filterInputsBy(s -> filterPattern.matcher(s).matches());
        }
        Reflections reflections = new Reflections(configuration);
        return reflections.getTypesAnnotatedWith(RetrofitBuilder.class);
    }


}
