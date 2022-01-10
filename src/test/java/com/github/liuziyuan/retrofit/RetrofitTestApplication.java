package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.annotation.EnableRetrofit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import retrofit2.Retrofit;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:51 PM
 */
//@EnableRetrofit("com.github.liuziyuan.retrofit.demo.api")
@EnableRetrofit
@SpringBootApplication
public class RetrofitTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetrofitTestApplication.class, args);
    }
}
