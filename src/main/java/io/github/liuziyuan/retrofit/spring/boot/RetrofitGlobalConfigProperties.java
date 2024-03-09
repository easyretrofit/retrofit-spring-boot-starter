package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.builder.*;
import io.github.liuziyuan.retrofit.core.util.BooleanUtil;
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
 * Spring boot web配置文件中声明的全局配置
 */
@Component
@Getter
@Setter
@ConfigurationProperties(
        prefix = "retrofit.global"
)
@Slf4j
public class RetrofitGlobalConfigProperties {

    private String enable;

    private OverrideRule overwriteType;

    private String baseUrl;

    private Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryBuilderClazz;

    private Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClazz;

    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;

    private Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;

    private Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;

    private String validateEagerly;

    public RetrofitGlobalConfigProperties(Environment environment) {
        this.enable = resolveRequiredPlaceholders(environment, "${retrofit.global.enable}");
        this.baseUrl = resolveRequiredPlaceholders(environment, "${retrofit.global.base-url}");
        this.callAdapterFactoryBuilderClazz = (Class<? extends BaseCallAdapterFactoryBuilder>[]) transformClasses(resolveRequiredPlaceholders(environment, "${retrofit.global.call-adapter-factory-builder-clazz}"));
        this.converterFactoryBuilderClazz = (Class<? extends BaseConverterFactoryBuilder>[]) transformClasses(resolveRequiredPlaceholders(environment, "${retrofit.global.converter-factory-builder-clazz}"));
        this.okHttpClientBuilderClazz = (Class<? extends BaseOkHttpClientBuilder>) transformClass(resolveRequiredPlaceholders(environment, "${retrofit.global.ok-http-client-builder-clazz}"));
        this.callBackExecutorBuilderClazz = (Class<? extends BaseCallBackExecutorBuilder>) transformClass(resolveRequiredPlaceholders(environment, "${retrofit.global.call-back-executor-builder-clazz}"));
        this.callFactoryBuilderClazz = (Class<? extends BaseCallFactoryBuilder>) transformClass(resolveRequiredPlaceholders(environment, "${retrofit.global.call-factory-builder-clazz}"));
        this.validateEagerly = resolveRequiredPlaceholders(environment, "${retrofit.global.validate-eagerly}");
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
