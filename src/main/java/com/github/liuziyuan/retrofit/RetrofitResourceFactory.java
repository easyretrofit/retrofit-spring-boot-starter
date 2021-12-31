package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.context.RetrofitClientBean;

/**
 * @author liuziyuan
 * @date 12/31/2021 5:02 PM
 */
public class RetrofitResourceFactory {

    public void RetrofitResourceFactory() {
        RetrofitClientBean retrofitClient = new RetrofitClientBean();
        RetrofitResourceContext context = new RetrofitResourceContext();
        context.addRetrofitClients(retrofitClient);
    }
}
