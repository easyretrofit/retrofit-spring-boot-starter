package com.github.liuziyuan.retrofit.builder;

import okhttp3.OkHttpClient;

/**
 * @author liuziyuan
 * @date 12/31/2021 12:57 PM
 */
public abstract class OkHttpClientBuilder extends BaseBuilder<OkHttpClient> {
    private OkHttpClient.Builder okHttpClientBuilder;

    /**
     * provide OkHttpClient.Builder to complete the OkHttpClient creation
     *
     * @param builder OkHttpClient.Builder
     * @return
     */
    public abstract OkHttpClient.Builder builder(OkHttpClient.Builder builder);

    @Override
    public final OkHttpClient executeBuild() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        return builder(okHttpClientBuilder).build();
    }
}
