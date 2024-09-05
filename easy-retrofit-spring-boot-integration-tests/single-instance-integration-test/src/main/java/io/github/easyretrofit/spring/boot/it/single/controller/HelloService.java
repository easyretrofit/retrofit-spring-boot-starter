package io.github.easyretrofit.spring.boot.it.single.controller;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String hello(String message) {
        return "Hello " + message;
    }
}
