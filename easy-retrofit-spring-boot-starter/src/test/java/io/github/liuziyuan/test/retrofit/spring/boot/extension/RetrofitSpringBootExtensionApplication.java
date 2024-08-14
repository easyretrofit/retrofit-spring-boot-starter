package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.spring.boot.EnableRetrofit;
import org.junit.Assert;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:51 PM
 */
@EnableRetrofit({"io.github.liuziyuan.test.retrofit.spring.boot.extension"})
@SpringBootApplication
public class RetrofitSpringBootExtensionApplication {
    public static void main(String[] args) throws NoSuchMethodException {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitSpringBootExtensionApplication.class, args);
        ListableBeanFactory beanFactory = run.getBeanFactory();

        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);
        List<RetrofitClientBean> retrofitClients = bean.getRetrofitClients();
        Assert.assertEquals(1, retrofitClients.size());
//        boolean b = bean.getRetrofitApiServiceBean(HelloApi.class).getInterceptors().stream().anyMatch(i -> i instanceof RetrofitTestInterceptor);
//        Assert.assertTrue(b);
    }
}
