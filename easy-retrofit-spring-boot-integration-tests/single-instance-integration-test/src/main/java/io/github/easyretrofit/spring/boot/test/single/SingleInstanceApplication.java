package io.github.easyretrofit.spring.boot.test.single;

import io.github.easyretrofit.spring.boot.EnableRetrofit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liuziyuan
 */
@Slf4j
@EnableRetrofit(basePackages = "io.github.easyretrofit.spring.boot.test.single.api")
@SpringBootApplication
public class SingleInstanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SingleInstanceApplication.class, args);
    }
}
