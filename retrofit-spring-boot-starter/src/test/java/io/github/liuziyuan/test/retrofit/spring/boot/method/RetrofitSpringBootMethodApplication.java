package io.github.liuziyuan.test.retrofit.spring.boot.method;

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
@EnableRetrofit({"io.github.liuziyuan.test.retrofit.spring.boot.method"})
@SpringBootApplication
public class RetrofitSpringBootMethodApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitSpringBootMethodApplication.class, args);
        BeanFactory beanFactory = run.getBeanFactory();
        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);

    }
}
