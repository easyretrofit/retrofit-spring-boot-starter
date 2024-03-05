package io.github.liuziyuan.retrofit.springboot;

import io.github.liuziyuan.retrofit.core.GlobalParamConfig;
import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.builder.*;

public class GlobalParamConfigSetting implements GlobalParamConfig {

    private final RetrofitGlobalConfigProperties properties;

    public GlobalParamConfigSetting(RetrofitGlobalConfigProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean enable() {
        return properties.isEnable();
    }

    @Override
    public OverrideRule overwriteType() {
        return OverrideRule.GLOBAL_FIRST;
    }

    @Override
    public String globalBaseUrl() {
        return properties.getBaseUrl();
    }

    @Override
    public Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz() {
        return properties.getCallAdapterFactoryBuilderClazz();
    }

    @Override
    public Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz() {
        return properties.getConverterFactoryBuilderClazz();
    }

    @Override
    public Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz() {
        return properties.getOkHttpClientBuilderClazz();
    }

    @Override
    public Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz() {
        return properties.getCallBackExecutorBuilderClazz();
    }

    @Override
    public Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz() {
        return properties.getCallFactoryBuilderClazz();
    }

    @Override
    public String globalValidateEagerly() {
        return properties.getValidateEagerly();
    }
}
