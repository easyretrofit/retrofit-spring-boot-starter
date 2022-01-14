package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.extension.OkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:16 AM
 */
public class OkHttpClientBuilderHandler implements Handler<OkHttpClient.Builder> {
    private Class<? extends OkHttpClientBuilder> okHttpClientBuilder;

    public OkHttpClientBuilderHandler(Class<? extends OkHttpClientBuilder> okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
    }

    @Override
    public OkHttpClient.Builder generate() {
        try {
            final OkHttpClientBuilder okHttpClientBuilder = this.okHttpClientBuilder.newInstance();
            return okHttpClientBuilder.executeBuild();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
