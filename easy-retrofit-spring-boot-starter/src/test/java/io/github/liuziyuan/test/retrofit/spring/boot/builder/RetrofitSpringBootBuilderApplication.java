package io.github.liuziyuan.test.retrofit.spring.boot.builder;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.resource.RetrofitBuilderBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.spring.boot.EnableRetrofit;
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
@EnableRetrofit({"io.github.liuziyuan.test.retrofit.spring.boot.builder"})
@SpringBootApplication
public class RetrofitSpringBootBuilderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitSpringBootBuilderApplication.class, args);
        BeanFactory beanFactory = run.getBeanFactory();
        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);
        List<RetrofitClientBean> retrofitClients = bean.getRetrofitClients();
        Assert.assertEquals(1, retrofitClients.size());
        RetrofitBuilderBean retrofitBuilder = retrofitClients.get(0).getRetrofitBuilder();
        Assert.assertEquals(1, retrofitBuilder.getAddCallAdapterFactory().length);
        SpringApplication.exit(run);
    }
}
