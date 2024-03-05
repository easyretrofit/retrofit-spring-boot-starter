package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.builder.*;

public interface GlobalParamConfig {

    boolean enable();

    OverrideRule overwriteType();

    String globalBaseUrl();

    Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz();

    Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz();

    Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz();

    Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz();

    Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz();

    String globalValidateEagerly();
}
