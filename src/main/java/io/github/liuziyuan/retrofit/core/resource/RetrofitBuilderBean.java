package io.github.liuziyuan.retrofit.core.resource;

import io.github.liuziyuan.retrofit.core.OverrideRule;
import io.github.liuziyuan.retrofit.core.builder.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrofitBuilderBean {
    private boolean enable = false;

    private OverrideRule overwriteType = OverrideRule.GLOBAL_FIRST;

    private String baseUrl = "";

    private Class<? extends BaseCallAdapterFactoryBuilder>[] addCallAdapterFactory;

    private Class<? extends BaseConverterFactoryBuilder>[] addConverterFactory;

    private Class<? extends BaseOkHttpClientBuilder> client;

    private Class<? extends BaseCallBackExecutorBuilder> callbackExecutor;

    private Class<? extends BaseCallFactoryBuilder> callFactory;

    private String validateEagerly;
}
