package com.github.liuziyuan.retrofit.demo;

import com.github.liuziyuan.retrofit.extension.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * @author liuziyuan
 * @date 12/31/2021 11:54 AM
 */
public class MyOkHttpClient extends BaseOkHttpClientBuilder {

    @Override
    public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {
        return builder;
    }
}
