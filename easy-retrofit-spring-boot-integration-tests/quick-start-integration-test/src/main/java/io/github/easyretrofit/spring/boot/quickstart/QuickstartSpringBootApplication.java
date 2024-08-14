package io.github.easyretrofit.spring.boot.quickstart;

import io.github.easyretrofit.spring.boot.EnableRetrofit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRetrofit
@SpringBootApplication
public class QuickstartSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickstartSpringBootApplication.class, args);
    }
}
