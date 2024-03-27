package io.github.liuziyuan.test.retrofit.spring.boot.global;

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
@EnableRetrofit({"io.github.liuziyuan.test.retrofit.spring.boot.global"})
@SpringBootApplication
public class RetrofitSpringBootGlobalApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitSpringBootGlobalApplication.class, args);
        BeanFactory beanFactory = run.getBeanFactory();
        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);
        List<RetrofitClientBean> retrofitClients = bean.getRetrofitClients();
        Assert.assertEquals(4, retrofitClients.size());
    }
}
