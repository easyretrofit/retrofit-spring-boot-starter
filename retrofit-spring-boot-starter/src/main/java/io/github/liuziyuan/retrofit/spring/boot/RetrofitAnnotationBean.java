package io.github.liuziyuan.retrofit.spring.boot;

import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class RetrofitAnnotationBean {

    private Set<Class<?>> retrofitBuilderClassSet;

    private List<String> basePackages;

    public RetrofitAnnotationBean(List<String> basePackages, Set<Class<?>> retrofitBuilderClassSet) {
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
        this.basePackages = basePackages;
    }

}
