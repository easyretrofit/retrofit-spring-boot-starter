package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class RetrofitAnnotationBean {

    private final Set<Class<?>> retrofitBuilderClassSet;

    private final List<String> basePackages;

    private final RetrofitResourceScanner.RetrofitExtension retrofitExtension;

    public RetrofitAnnotationBean(List<String> basePackages, Set<Class<?>> retrofitBuilderClassSet, RetrofitResourceScanner.RetrofitExtension retrofitExtension) {
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
        this.basePackages = basePackages;
        this.retrofitExtension = retrofitExtension;
    }

}
