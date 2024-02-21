package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.annotation.EnableRetrofit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:51 PM
 */
@EnableRetrofit({"io.github.liuziyuan.retrofit.demo.api", "io.github.liuziyuan.retrofit.injectdemo.api"})
//@EnableRetrofit
@SpringBootApplication
public class RetrofitTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetrofitTestApplication.class, args);
    }
}
