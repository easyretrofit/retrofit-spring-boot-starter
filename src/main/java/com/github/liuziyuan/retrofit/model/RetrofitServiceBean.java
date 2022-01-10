package com.github.liuziyuan.retrofit.model;

/**
 * @author liuziyuan
 * @date 12/31/2021 5:54 PM
 */
public class RetrofitServiceBean {

    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private RetrofitClientBean retrofitClientBean;

    public RetrofitClientBean getRetrofitClientBean() {
        return retrofitClientBean;
    }

    void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBean = retrofitClientBean;
    }

    public Class<?> getSelfClazz() {
        return selfClazz;
    }

    public void setSelfClazz(Class<?> selfClazz) {
        this.selfClazz = selfClazz;
    }

    public Class<?> getParentClazz() {
        return parentClazz;
    }

    public void setParentClazz(Class<?> parentClazz) {
        this.parentClazz = parentClazz;
    }

}
