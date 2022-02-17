package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.extension.BaseCallFactoryBuilder;
import lombok.SneakyThrows;
import okhttp3.Call;

/**
 * @author liuziyuan
 */
public class CallFactoryGenerator implements Generator<Call.Factory> {
    private final Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;
    private final Call.Factory factory;

    public CallFactoryGenerator(Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz, Call.Factory factory) {
        this.callFactoryBuilderClazz = callFactoryBuilderClazz;
        this.factory = factory;
    }

    @SneakyThrows
    @Override
    public Call.Factory generate() {
        if (factory != null) {
            return factory;
        }
        if (callFactoryBuilderClazz != null) {
            final String baseCallFactoryBuilderClazzName = BaseCallFactoryBuilder.class.getName();
            final String callFactoryBuilderClazzName = callFactoryBuilderClazz.getName();
            if (baseCallFactoryBuilderClazzName.equals(callFactoryBuilderClazzName)) {
                return null;
            } else {
                final BaseCallFactoryBuilder builder = callFactoryBuilderClazz.newInstance();
                return builder.executeBuild();
            }
        }
        return null;
    }
}
