package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.GlobalParamConfig;
import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.builder.*;

/**
 * 继承core中的GlobalParamConfig实现自定义Retrofit必要的的全局配置
 * 这个类合并了自定义的配置和web的resources文件夹中配置文件中的配置
 * 合并原则是：以下优先级：resources文件夹中配置 > 自定义配置
 */
public class GlobalParamConfigSetting implements GlobalParamConfig {

    private final RetrofitGlobalConfigProperties properties;
    private final GlobalParamConfig customSetting;

    public GlobalParamConfigSetting(RetrofitGlobalConfigProperties properties, GlobalParamConfig customSetting) {
        this.properties = properties;
        this.customSetting = customSetting;
    }

    @Override
    public boolean enable() {
        if (properties != null) {
            return properties.isEnable();
        }
        return customSetting.enable();
    }

    @Override
    public OverrideRule overwriteType() {
        OverrideRule overwriteType = null;
        if (properties.getOverwriteType() != null) {
            overwriteType = properties.getOverwriteType();
        } else {
            overwriteType = customSetting.overwriteType();
        }
        if (overwriteType == null) {
            overwriteType = OverrideRule.GLOBAL_FIRST;
        }
        return overwriteType;
    }

    @Override
    public String globalBaseUrl() {
        if (properties.getBaseUrl() != null) {
            return properties.getBaseUrl();
        }
        return customSetting.globalBaseUrl();
    }

    @Override
    public Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz() {
        if (properties.getCallAdapterFactoryBuilderClazz() != null) {
            return properties.getCallAdapterFactoryBuilderClazz();
        }
        return customSetting.globalCallAdapterFactoryBuilderClazz();
    }

    @Override
    public Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz() {
        if (properties.getConverterFactoryBuilderClazz() != null) {
            return properties.getConverterFactoryBuilderClazz();
        }
        return customSetting.globalConverterFactoryBuilderClazz();
    }

    @Override
    public Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz() {
        if (properties.getOkHttpClientBuilderClazz() != null) {
            return properties.getOkHttpClientBuilderClazz();
        }
        return customSetting.globalOkHttpClientBuilderClazz();
    }

    @Override
    public Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz() {
        if (properties.getCallBackExecutorBuilderClazz() != null) {
            return properties.getCallBackExecutorBuilderClazz();
        }
        return customSetting.globalCallBackExecutorBuilderClazz();
    }

    @Override
    public Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz() {
        if (properties.getCallFactoryBuilderClazz() != null) {
            return properties.getCallFactoryBuilderClazz();
        }
        return customSetting.globalCallFactoryBuilderClazz();
    }

    @Override
    public String globalValidateEagerly() {
        if (properties.getValidateEagerly() != null) {
            return properties.getValidateEagerly();
        }
        return customSetting.globalValidateEagerly();
    }
}
