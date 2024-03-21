package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.builder.*;
import io.github.liuziyuan.retrofit.core.util.BooleanUtil;

/**
 * 这个类合并了自定义的配置和web的resources文件夹中配置文件中的配置
 * 合并原则是：以下优先级：resources文件夹中配置大于自定义配置，如果resources文件夹中没有配置，则使用自定义配置
 */
public class SpringBootGlobalConfig implements RetrofitBuilderExtension {

    private final RetrofitGlobalConfigProperties properties;
    private final RetrofitBuilderExtension customConfig;

    public SpringBootGlobalConfig(RetrofitGlobalConfigProperties properties, RetrofitBuilderExtension customConfig) {
        this.properties = properties;
        this.customConfig = customConfig;
    }

    @Override
    public boolean enable() {
        if (properties.getEnable() != null) {
            return BooleanUtil.transformToBoolean(properties.getEnable());
        }
        return customConfig != null && customConfig.enable();
    }

    private boolean propertiesEnable() {
        return BooleanUtil.transformToBoolean(properties.getEnable());
    }

    @Override
    public OverrideRule overwriteType() {
        OverrideRule overwriteType = null;
        if (propertiesEnable() && properties.getOverwriteType() != null) {
            overwriteType = properties.getOverwriteType();
        } else {
            overwriteType = customConfig == null ? OverrideRule.GLOBAL_FIRST : customConfig.overwriteType();
        }
        if (overwriteType == null) {
            overwriteType = OverrideRule.GLOBAL_FIRST;
        }
        return overwriteType;
    }

    @Override
    public String globalBaseUrl() {
        if (propertiesEnable() && properties.getBaseUrl() != null) {
            return properties.getBaseUrl();
        }
        return customConfig == null ? null : customConfig.globalBaseUrl();
    }

    @Override
    public Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz() {
        if (propertiesEnable() && properties.getCallAdapterFactoryBuilderClazz() != null) {
            return properties.getCallAdapterFactoryBuilderClazz();
        }
        return customConfig == null ? null : customConfig.globalCallAdapterFactoryBuilderClazz();
    }

    @Override
    public Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz() {
        if (propertiesEnable() && properties.getConverterFactoryBuilderClazz() != null) {
            return properties.getConverterFactoryBuilderClazz();
        }
        return customConfig == null ? null : customConfig.globalConverterFactoryBuilderClazz();
    }

    @Override
    public Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz() {
        if (propertiesEnable() && properties.getOkHttpClientBuilderClazz() != null) {
            return properties.getOkHttpClientBuilderClazz();
        }
        return customConfig == null ? null : customConfig.globalOkHttpClientBuilderClazz();
    }

    @Override
    public Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz() {
        if (propertiesEnable() && properties.getCallBackExecutorBuilderClazz() != null) {
            return properties.getCallBackExecutorBuilderClazz();
        }
        return customConfig == null ? null : customConfig.globalCallBackExecutorBuilderClazz();
    }

    @Override
    public Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz() {
        if (propertiesEnable() && properties.getCallFactoryBuilderClazz() != null) {
            return properties.getCallFactoryBuilderClazz();
        }
        return customConfig == null ? null : customConfig.globalCallFactoryBuilderClazz();
    }

    @Override
    public String globalValidateEagerly() {
        if (propertiesEnable() && properties.getValidateEagerly() != null) {
            return properties.getValidateEagerly();
        }
        return customConfig == null ? null : customConfig.globalValidateEagerly();
    }
}
