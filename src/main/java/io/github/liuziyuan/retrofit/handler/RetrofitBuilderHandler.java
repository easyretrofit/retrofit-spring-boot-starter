package io.github.liuziyuan.retrofit.handler;

import io.github.liuziyuan.retrofit.Handler;
import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.extension.UrlOverWriteInterceptor;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.util.CollectionUtils;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuziyuan
 */
public class RetrofitBuilderHandler implements Handler<Retrofit.Builder> {
    private RetrofitClientBean clientBean;
    private RetrofitResourceContext context;

    public RetrofitBuilderHandler(RetrofitClientBean clientBean, RetrofitResourceContext context) {
        this.clientBean = clientBean;
        this.context = context;
    }

    @Override
    public Retrofit.Builder generate() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(clientBean.getRealHostUrl());
        setRetrofitCallAdapterFactory(clientBean, builder);
        setRetrofitConverterFactory(clientBean, builder);
        setRetrofitOkHttpClient(clientBean, builder);
        return builder;
    }

    private void setRetrofitCallAdapterFactory(RetrofitClientBean clientBean, Retrofit.Builder builder) {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<CallAdapter.Factory> callAdapterFactories = getCallAdapterFactories(retrofitBuilder.addCallAdapterFactory());
        if (!CollectionUtils.isEmpty(callAdapterFactories)) {
            callAdapterFactories.forEach(builder::addCallAdapterFactory);
        }
    }

    private void setRetrofitConverterFactory(RetrofitClientBean clientBean, Retrofit.Builder builder) {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<Converter.Factory> converterFactories = getConverterFactories(retrofitBuilder.addConverterFactory());
        if (!CollectionUtils.isEmpty(converterFactories)) {
            converterFactories.forEach(builder::addConverterFactory);
        }
    }

    @SneakyThrows
    private void setRetrofitOkHttpClient(RetrofitClientBean clientBean, Retrofit.Builder builder) {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<RetrofitInterceptor> interceptors = clientBean.getInterceptors();
        OkHttpClient.Builder okHttpClientBuilder;
        OkHttpClientBuilderHandler okHttpClientBuilderHandler;
        if (retrofitBuilder.client() != null) {
            okHttpClientBuilderHandler = new OkHttpClientBuilderHandler(retrofitBuilder.client());
            okHttpClientBuilder = okHttpClientBuilderHandler.generate();
        } else {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        okHttpClientBuilder.addInterceptor(new UrlOverWriteInterceptor(context));
        final List<Interceptor> okHttpInterceptors = getOkHttpInterceptors(interceptors);

        okHttpInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        builder.client(okHttpClientBuilder.build());
    }

    @SneakyThrows
    private List<Interceptor> getOkHttpInterceptors(List<RetrofitInterceptor> interceptors) {
        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpInterceptorHandler okHttpInterceptorHandler;
        for (RetrofitInterceptor interceptor : interceptors) {
            okHttpInterceptorHandler = new OkHttpInterceptorHandler(interceptor.handler(), context);
            final Interceptor generateInterceptor = okHttpInterceptorHandler.generate();
            interceptorList.add(generateInterceptor);
        }
        return interceptorList;
    }

    @SneakyThrows
    private List<CallAdapter.Factory> getCallAdapterFactories(Class<? extends CallAdapter.Factory>[] callAdapterFactoryClasses) {
        List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        CallAdapterFactoryHandler adapterFactoryHandler;
        for (Class<? extends CallAdapter.Factory> callAdapterFactoryClass : callAdapterFactoryClasses) {
            adapterFactoryHandler = new CallAdapterFactoryHandler(callAdapterFactoryClass);
            callAdapterFactories.add(adapterFactoryHandler.generate());
        }
        return callAdapterFactories;
    }

    @SneakyThrows
    private List<Converter.Factory> getConverterFactories(Class<? extends Converter.Factory>[] converterFactoryClasses) {
        List<Converter.Factory> converterFactories = new ArrayList<>();
        ConverterFactoryHandler converterFactoryHandler;
        for (Class<? extends Converter.Factory> converterFactoryClass : converterFactoryClasses) {
            converterFactoryHandler = new ConverterFactoryHandler(converterFactoryClass);
            converterFactories.add(converterFactoryHandler.generate());
        }
        return converterFactories;
    }


}