package io.github.easyretrofit.spring.boot.test.inherit;

import io.github.easyretrofit.spring.boot.EnableRetrofit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRetrofit
@SpringBootApplication
public class InheritSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(InheritSpringBootApplication.class, args);
    }
}
