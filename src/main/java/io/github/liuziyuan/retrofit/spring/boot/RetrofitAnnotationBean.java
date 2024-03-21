package io.github.liuziyuan.retrofit.spring.boot;

import lombok.Getter;

import java.util.Set;

@Getter
public class RetrofitAnnotationBean {

    private Set<Class<?>> retrofitBuilderClassSet;

    public RetrofitAnnotationBean(Set<Class<?>> retrofitBuilderClassSet) {
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
    }

}
