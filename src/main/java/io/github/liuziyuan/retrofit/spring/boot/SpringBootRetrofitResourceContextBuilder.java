package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.Extension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContextBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

/**
 * Core中RetrofitResourceContextBuilder的实现类，这里核心的目的就是扩展Extension
 */
public class SpringBootRetrofitResourceContextBuilder extends RetrofitResourceContextBuilder {

    private SpringBootRetrofitResourceScanner scanner;

    public SpringBootRetrofitResourceContextBuilder(Env env, SpringBootRetrofitResourceScanner scanner) {
        super(env);
        this.scanner = scanner;
    }

    @Override
    public List<Extension> registerExtension(List<Extension> extensions) {
        Set<Class<?>> classes = scanner.scanRetrofitComponentClasses("io.github.liuziyuan.retrofit.core.Extension");
        for (Class<?> clazz : classes) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true); // 如果类或构造器是私有的，需要设置为可访问
                extensions.add((Extension) constructor.newInstance());
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return extensions;
    }


}
