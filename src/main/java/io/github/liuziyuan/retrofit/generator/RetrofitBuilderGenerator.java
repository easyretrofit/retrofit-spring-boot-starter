package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.annotation.InterceptorType;
import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.extension.*;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Generate RetrofitBuilder instance
 *
 * @author liuziyuan
 */
public class RetrofitBuilderGenerator implements Generator<Retrofit.Builder> {
    private final RetrofitClientBean clientBean;
    private final RetrofitResourceContext context;
    private final Retrofit.Builder builder;

    public RetrofitBuilderGenerator(RetrofitClientBean clientBean, RetrofitResourceContext context) {
        this.builder = new Retrofit.Builder();
        this.clientBean = clientBean;
        this.context = context;
    }

    @Override
    public Retrofit.Builder generate() {
        setBaseUrl();
        setRetrofitCallAdapterFactory();
        setRetrofitConverterFactory();
        setCallBackExecutor();
        setValidateEagerly();
        setRetrofitOkHttpClient();
        setCallFactory();
        return builder;
    }


    private void setBaseUrl() {
        builder.baseUrl(clientBean.getRealHostUrl());
    }

    private void setValidateEagerly() {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final boolean validateEagerly = retrofitBuilder.validateEagerly();
        builder.validateEagerly(validateEagerly);
    }

    private void setCallFactory() {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz = retrofitBuilder.callFactory();
        final ApplicationContext applicationContext = context.getApplicationContext();
        CallFactoryGenerator callFactoryGenerator = new CallFactoryGenerator(callFactoryBuilderClazz, applicationContext);
        final Call.Factory factory = callFactoryGenerator.generate();
        if (factory != null) {
            builder.callFactory(factory);
        }
    }

    private void setCallBackExecutor() {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final Class<? extends BaseCallBackExecutorBuilder> callbackExecutorBuilderClazz = retrofitBuilder.callbackExecutor();
        final ApplicationContext applicationContext = context.getApplicationContext();
        CallBackExecutorGenerator callBackExecutorGenerator = new CallBackExecutorGenerator(callbackExecutorBuilderClazz, applicationContext);
        final Executor executor = callBackExecutorGenerator.generate();
        if (executor != null) {
            builder.callbackExecutor(executor);
        }
    }

    private void setRetrofitCallAdapterFactory() {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<CallAdapter.Factory> callAdapterFactories = getCallAdapterFactories(retrofitBuilder.addCallAdapterFactory());
        if (!CollectionUtils.isEmpty(callAdapterFactories)) {
            callAdapterFactories.forEach(builder::addCallAdapterFactory);
        }
    }

    private void setRetrofitConverterFactory() {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<Converter.Factory> converterFactories = getConverterFactories(retrofitBuilder.addConverterFactory());
        if (!CollectionUtils.isEmpty(converterFactories)) {
            converterFactories.forEach(builder::addConverterFactory);
        }
    }

    @SneakyThrows
    private void setRetrofitOkHttpClient() {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        Set<RetrofitInterceptor> allInterceptors = new LinkedHashSet<>();
        allInterceptors.addAll(clientBean.getInterceptors());
        allInterceptors.addAll(clientBean.getInheritedInterceptors());
        final List<RetrofitInterceptor> interceptors = new ArrayList<>(allInterceptors);
        OkHttpClient.Builder okHttpClientBuilder;
        if (retrofitBuilder.client() != null) {
            final ApplicationContext applicationContext = context.getApplicationContext();
            final OkHttpClientBuilderGenerator clientBuilderGenerator = new OkHttpClientBuilderGenerator(retrofitBuilder.client(), applicationContext);
            okHttpClientBuilder = clientBuilderGenerator.generate();
        } else {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        okHttpClientBuilder.addInterceptor(new DynamicBaseUrlInterceptor(context));
        okHttpClientBuilder.addInterceptor(new UrlOverWriteInterceptor(context));
        final List<Interceptor> okHttpDefaultInterceptors = getOkHttpInterceptors(interceptors, InterceptorType.DEFAULT);
        final List<Interceptor> okHttpNetworkInterceptors = getOkHttpInterceptors(interceptors, InterceptorType.NETWORK);
        okHttpDefaultInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        okHttpNetworkInterceptors.forEach(okHttpClientBuilder::addNetworkInterceptor);
        builder.client(okHttpClientBuilder.build());
    }

    @SneakyThrows
    private List<Interceptor> getOkHttpInterceptors(List<RetrofitInterceptor> interceptors, InterceptorType type) {
        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpInterceptorGenerator okHttpInterceptorGenerator;
        interceptors.sort(Comparator.comparing(RetrofitInterceptor::sort));
        for (RetrofitInterceptor interceptor : interceptors) {
            if (interceptor.type() == type) {
                okHttpInterceptorGenerator = new OkHttpInterceptorGenerator(interceptor, context);
                final Interceptor generateInterceptor = okHttpInterceptorGenerator.generate();
                interceptorList.add(generateInterceptor);
            }
        }
        return interceptorList;
    }

    @SneakyThrows
    private List<CallAdapter.Factory> getCallAdapterFactories(Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryClasses) {
        List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        CallAdapterFactoryGenerator callAdapterFactoryGenerator;
        final ApplicationContext applicationContext = context.getApplicationContext();
        for (Class<? extends BaseCallAdapterFactoryBuilder> callAdapterFactoryClazz : callAdapterFactoryClasses) {
            callAdapterFactoryGenerator = new CallAdapterFactoryGenerator(callAdapterFactoryClazz, applicationContext);
            callAdapterFactories.add(callAdapterFactoryGenerator.generate());
        }
        return callAdapterFactories;
    }

    @SneakyThrows
    private List<Converter.Factory> getConverterFactories(Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClasses) {
        List<Converter.Factory> converterFactories = new ArrayList<>();
        ConverterFactoryGenerator converterFactoryGenerator;
        final ApplicationContext applicationContext = context.getApplicationContext();
        for (Class<? extends BaseConverterFactoryBuilder> converterFactoryBuilderClazz : converterFactoryBuilderClasses) {
            converterFactoryGenerator = new ConverterFactoryGenerator(converterFactoryBuilderClazz, applicationContext);
            converterFactories.add(converterFactoryGenerator.generate());
        }
        return converterFactories;
    }

}
