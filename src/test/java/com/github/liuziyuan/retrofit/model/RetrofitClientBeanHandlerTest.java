package com.github.liuziyuan.retrofit.model;

import com.github.liuziyuan.retrofit.demo.MyRetrofitInterceptor1;
import com.github.liuziyuan.retrofit.extension.Interceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuziyuan
 * @date 1/13/2022 12:05 PM
 */

class RetrofitClientBeanHandlerTest {

    private RetrofitClientBeanHandler handler;

    @BeforeEach
    void setUp() {

        List<RetrofitClientBean> retrofitClientBeanList = new ArrayList<>();
        RetrofitServiceBean retrofitServiceBean = new RetrofitServiceBean();
        List<Interceptor> interceptors = new ArrayList<>();

    }

    @Test
    void generate() {
    }
}