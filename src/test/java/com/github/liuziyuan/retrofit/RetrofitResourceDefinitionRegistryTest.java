package com.github.liuziyuan.retrofit;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author liuziyuan
 * @date 1/14/2022 5:47 PM
 */


class RetrofitResourceDefinitionRegistryTest {

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerBeanDefinitions() {
        final RetrofitResourceContext retrofitResourceContext = context.getBean(RetrofitResourceContext.class);
        Assert.assertNotNull(retrofitResourceContext);
    }
}