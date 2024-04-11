package io.github.liuziyuan.test.retrofit.spring.boot.web;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.spring.boot.EnableRetrofit;
import org.junit.Assert;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:51 PM
 */
@EnableRetrofit({"io.github.liuziyuan.test.retrofit.spring.boot.web"})
@SpringBootApplication
public class RetrofitSpringBootWebApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitSpringBootWebApplication.class, args);
        BeanFactory beanFactory = run.getBeanFactory();
        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);
        List<RetrofitClientBean> retrofitClients = bean.getRetrofitClients();
        Assert.assertEquals(1, retrofitClients.size());
        Assert.assertEquals(1, retrofitClients.get(0).getRetrofitBuilder().getAddConverterFactory().length);
        Assert.assertEquals(2, retrofitClients.get(0).getRetrofitBuilder().getAddCallAdapterFactory().length);
    }
}
