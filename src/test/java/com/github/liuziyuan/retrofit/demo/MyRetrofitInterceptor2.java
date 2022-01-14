package com.github.liuziyuan.retrofit.demo;

import com.github.liuziyuan.retrofit.RetrofitResourceContext;
import com.github.liuziyuan.retrofit.extension.BaseInterceptor;
import okhttp3.Response;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:44 PM
 */
public class MyRetrofitInterceptor2 extends BaseInterceptor {

    public MyRetrofitInterceptor2(RetrofitResourceContext context) {
        super(context);
    }

    @Override
    protected Response executeIntercept(Chain chain) {
        return null;
    }
}
