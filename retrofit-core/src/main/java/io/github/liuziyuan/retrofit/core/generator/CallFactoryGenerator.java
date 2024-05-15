package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.builder.BaseCallFactoryBuilder;
import okhttp3.Call;

/**
 * @author liuziyuan
 */
public abstract class CallFactoryGenerator implements Generator<Call.Factory> {
    private final Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;

    public CallFactoryGenerator(Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz) {
        this.callFactoryBuilderClazz = callFactoryBuilderClazz;
    }

    public abstract BaseCallFactoryBuilder buildInjectionObject(Class<? extends BaseCallFactoryBuilder> clazz);

    @Override
    public Call.Factory generate() {
        BaseCallFactoryBuilder baseCallFactoryBuilder = buildInjectionObject(callFactoryBuilderClazz);
        if (baseCallFactoryBuilder != null) {
            return baseCallFactoryBuilder.executeBuild();
        }
        if (callFactoryBuilderClazz != null) {
            final String baseCallFactoryBuilderClazzName = BaseCallFactoryBuilder.class.getName();
            final String callFactoryBuilderClazzName = callFactoryBuilderClazz.getName();
            if (baseCallFactoryBuilderClazzName.equals(callFactoryBuilderClazzName)) {
                return null;
            } else {
                final BaseCallFactoryBuilder builder;
                try {
                    builder = callFactoryBuilderClazz.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
                return builder.executeBuild();
            }
        }
        return null;
    }
}
