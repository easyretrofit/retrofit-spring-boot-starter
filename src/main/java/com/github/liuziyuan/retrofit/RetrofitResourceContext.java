package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.context.RetrofitClientBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuziyuan
 * @date 12/31/2021 3:34 PM
 */
public class RetrofitResourceContext {

    private List<RetrofitClientBean> retrofitClients;

    public RetrofitResourceContext() {
        retrofitClients = new ArrayList<>();
    }

    void addRetrofitClients(RetrofitClientBean retrofitClient) {
        retrofitClients.add(retrofitClient);
    }
}
