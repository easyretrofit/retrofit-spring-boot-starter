package com.github.liuziyuan.retrofit.model;

import java.util.List;

/**
 * @author liuziyuan
 * @date 12/31/2021 3:46 PM
 */
public class RetrofitClientBean {

    private List<RetrofitServiceBean> retrofitServices;
    private String retrofitInstanceName;

    public String getRetrofitInstanceName() {
        return this.retrofitInstanceName;
    }
}
