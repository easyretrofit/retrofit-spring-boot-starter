package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.extension.BaseInterceptor;
import okhttp3.Response;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:44 PM
 */
public class MyRetrofitInterceptor2 extends BaseInterceptor {
    @Override
    protected Response executeIntercept(Chain chain) {
        return null;
    }
}
