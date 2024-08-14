package io.github.easyretrofit.spring.boot.test.builder;

import io.github.easyretrofit.spring.boot.EnableRetrofit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRetrofit
@SpringBootApplication
public class RetrofitBuilderSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetrofitBuilderSpringBootApplication.class, args);
    }
}
