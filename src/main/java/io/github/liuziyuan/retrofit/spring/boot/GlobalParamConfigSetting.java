package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.GlobalParamConfig;
import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.builder.*;

public class GlobalParamConfigSetting implements GlobalParamConfig {

    private final RetrofitGlobalConfigProperties properties;
    private final GlobalParamConfig customSetting;

    public GlobalParamConfigSetting(RetrofitGlobalConfigProperties properties, GlobalParamConfig customSetting) {
        this.properties = properties;
        this.customSetting = customSetting;
    }

    @Override
    public boolean enable() {
        if (customSetting != null && customSetting.enable()) {
            return customSetting.enable();
        }
        return properties.isEnable();
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
