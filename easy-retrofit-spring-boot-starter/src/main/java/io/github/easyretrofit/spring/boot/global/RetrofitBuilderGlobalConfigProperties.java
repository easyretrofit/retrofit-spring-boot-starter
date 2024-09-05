package io.github.easyretrofit.spring.boot.global;

import io.github.easyretrofit.core.builder.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring boot resources文件夹中application.yml(properties)配置文件中声明的全局配置
 */
@Component
@Getter
@Setter
@ConfigurationProperties(
        prefix = "retrofit.global.builder"
)
@Slf4j
public class RetrofitBuilderGlobalConfigProperties {

    private String enable;

    private String baseUrl;

    private Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryBuilderClazz;

    private Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClazz;

    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;

    private Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;

    private Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;

    private String validateEagerly;

    public RetrofitBuilderGlobalConfigProperties generate(Environment environment) {
        this.enable = resolveRequiredPlaceholders(environment, "${retrofit.global.builder.enable}");
        this.baseUrl = resolveRequiredPlaceholders(environment, "${retrofit.global.builder.base-url}");
        this.callAdapterFactoryBuilderClazz = (Class<? extends BaseCallAdapterFactoryBuilder>[]) transformClasses(resolveRequiredPlaceholders(environment, "${retrofit.global.builder.call-adapter-factory-builder-clazz}"));
        this.converterFactoryBuilderClazz = (Class<? extends BaseConverterFactoryBuilder>[]) transformClasses(resolveRequiredPlaceholders(environment, "${retrofit.global.builder.converter-factory-builder-clazz}"));
        this.okHttpClientBuilderClazz = (Class<? extends BaseOkHttpClientBuilder>) transformClass(resolveRequiredPlaceholders(environment, "${retrofit.global.builder.ok-http-client-builder-clazz}"));
        this.callBackExecutorBuilderClazz = (Class<? extends BaseCallBackExecutorBuilder>) transformClass(resolveRequiredPlaceholders(environment, "${retrofit.global.builder.call-back-executor-builder-clazz}"));
        this.callFactoryBuilderClazz = (Class<? extends BaseCallFactoryBuilder>) transformClass(resolveRequiredPlaceholders(environment, "${retrofit.global.builder.call-factory-builder-clazz}"));
        this.validateEagerly = resolveRequiredPlaceholders(environment, "${retrofit.global.builder.validate-eagerly}");
        return this;
    }

    private String resolveRequiredPlaceholders(Environment environment, String text) {
        try {
            return environment.resolveRequiredPlaceholders(text);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Class<?>[] transformClasses(String className) {
        List<Class<?>> clazzList = new ArrayList<>();
        try {
            String[] clazzStrList = StringUtils.split(className, ",");
            for (String clazz : clazzStrList) {
                try {
                    clazzList.add(Class.forName(clazz.trim()));
                } catch (ClassNotFoundException | NullPointerException e) {
                    log.warn(e.toString());
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return clazzList.toArray(new Class<?>[0]);
    }

    private Class<?> transformClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException | NullPointerException e) {
            return null;
        }
    }

}
