package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.core.GlobalParamConfig;
import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.builder.*;
import io.github.liuziyuan.retrofit.injectdemo.InjectGsonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.injectdemo.InjectJacksonConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.injectdemo.InjectRxJavaCallAdapterFactoryBuilder;
import io.github.liuziyuan.retrofit.spring.boot.RetrofitComponent;

import java.util.ArrayList;

@RetrofitComponent
public class MyCustomGlobalParamConfigSetting implements GlobalParamConfig {

    @Override
    public boolean enable() {
        return true;
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
        return new ArrayList<Class<? extends BaseCallAdapterFactoryBuilder>>() {{
            add(InjectRxJavaCallAdapterFactoryBuilder.class);
        }}.toArray(new Class[0]);
    }

    @Override
    public Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz() {
        return new ArrayList<Class<? extends BaseConverterFactoryBuilder>>() {{
            add(InjectGsonConverterFactoryBuilder.class);
            add(InjectJacksonConverterFactoryBuilder.class);
        }}.toArray(new Class[0]);
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
