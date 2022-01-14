package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.resource.RetrofitClientBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuziyuan
 * @date 12/31/2021 3:34 PM
 */
public final class RetrofitResourceContext {


    private List<RetrofitClientBean> retrofitClients;

    public RetrofitResourceContext() {
        retrofitClients = new ArrayList<>();
    }

    void setRetrofitClients(List<RetrofitClientBean> retrofitClients) {
        this.retrofitClients = retrofitClients;
    }

    List<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

}
