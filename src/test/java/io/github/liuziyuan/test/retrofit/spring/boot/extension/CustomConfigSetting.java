package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.builder.*;
import org.springframework.stereotype.Component;

@Component
public class CustomConfigSetting implements RetrofitBuilderExtension {
    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public OverrideRule overwriteType() {
        return null;
    }

    @Override
    public String globalBaseUrl() {
        return null;
    }

    @Override
    public Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz() {
        return new Class[0];
    }

    @Override
    public Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz() {
        return new Class[0];
    }

    @Override
    public Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz() {
        return null;
    }

    @Override
    public Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz() {
        return null;
    }

    @Override
    public Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz() {
        return null;
    }

    @Override
    public String globalValidateEagerly() {
        return null;
    }
}
