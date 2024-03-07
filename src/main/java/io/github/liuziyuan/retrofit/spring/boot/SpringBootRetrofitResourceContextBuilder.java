package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.Extension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContextBuilder;

import java.util.List;

/**
 * Core中RetrofitResourceContextBuilder的实现类，这里核心的目的就是扩展Extension
 */
public class SpringBootRetrofitResourceContextBuilder extends RetrofitResourceContextBuilder {

    @Override
    public List<Extension> registerExtension(List<Extension> extensions) {
        return extensions;
    }

    public SpringBootRetrofitResourceContextBuilder(Env env) {
        super(env);
    }


}
