package com.github.liuziyuan.retrofit.extension;

import okhttp3.OkHttpClient;

/**
 * @author liuziyuan
 * @date 12/31/2021 12:57 PM
 */
public abstract class BaseOkHttpClientBuilder extends BaseBuilder<OkHttpClient.Builder> {

    /**
     * provide OkHttpClient.Builder to complete the OkHttpClient creation
     *
     * @param builder OkHttpClient.Builder
     * @return OkHttpClient.Builder
     */
    public abstract OkHttpClient.Builder builder(OkHttpClient.Builder builder);

    @Override
    public final OkHttpClient.Builder executeBuild() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        return builder(okHttpClientBuilder);
    }


}
