package io.github.easyretrofit.spring.boot.global;

import io.github.easyretrofit.core.builder.*;
import io.github.easyretrofit.spring.boot.RetrofitResourceDefinitionRegistry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring boot resources文件夹中application.yml(properties)配置文件中声明的全局配置
 */
@Component
@ConfigurationProperties(
        prefix = "retrofit.global.builder"
)
public class RetrofitBuilderGlobalConfigProperties {

    Logger log = LoggerFactory.getLogger(RetrofitBuilderGlobalConfigProperties.class);

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

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Class<? extends BaseCallAdapterFactoryBuilder>[] getCallAdapterFactoryBuilderClazz() {
        return callAdapterFactoryBuilderClazz;
    }

    public void setCallAdapterFactoryBuilderClazz(Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryBuilderClazz) {
        this.callAdapterFactoryBuilderClazz = callAdapterFactoryBuilderClazz;
    }

    public Class<? extends BaseConverterFactoryBuilder>[] getConverterFactoryBuilderClazz() {
        return converterFactoryBuilderClazz;
    }

    public void setConverterFactoryBuilderClazz(Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClazz) {
        this.converterFactoryBuilderClazz = converterFactoryBuilderClazz;
    }

    public Class<? extends BaseOkHttpClientBuilder> getOkHttpClientBuilderClazz() {
        return okHttpClientBuilderClazz;
    }

    public void setOkHttpClientBuilderClazz(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz) {
        this.okHttpClientBuilderClazz = okHttpClientBuilderClazz;
    }

    public Class<? extends BaseCallBackExecutorBuilder> getCallBackExecutorBuilderClazz() {
        return callBackExecutorBuilderClazz;
    }

    public void setCallBackExecutorBuilderClazz(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
    }

    public Class<? extends BaseCallFactoryBuilder> getCallFactoryBuilderClazz() {
        return callFactoryBuilderClazz;
    }

    public void setCallFactoryBuilderClazz(Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz) {
        this.callFactoryBuilderClazz = callFactoryBuilderClazz;
    }

    public String getValidateEagerly() {
        return validateEagerly;
    }

    public void setValidateEagerly(String validateEagerly) {
        this.validateEagerly = validateEagerly;
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
