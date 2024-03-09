package io.github.liuziyuan.retrofit.spring.boot;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RetrofitAnnotationBean {

    Set<Class<?>> retrofitBuilderClassSet;

    public RetrofitAnnotationBean(Set<Class<?>> retrofitBuilderClassSet) {
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
    }
}
