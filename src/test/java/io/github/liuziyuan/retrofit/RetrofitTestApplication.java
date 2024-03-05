package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.springboot.EnableRetrofit;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:51 PM
 */
@EnableRetrofit({"io.github.liuziyuan.retrofit.demo.api", "io.github.liuziyuan.retrofit.injectdemo.api"})
//@EnableRetrofit
@SpringBootApplication
public class RetrofitTestApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitTestApplication.class, args);
        BeanFactory beanFactory = run.getBeanFactory();
        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);
        List<RetrofitClientBean> retrofitClients = bean.getRetrofitClients();
    }
}
