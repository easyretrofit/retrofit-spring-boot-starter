package io.github.easyretrofit.spring.boot.test.single.controller;

import io.github.easyretrofit.spring.boot.test.single.api.HelloApi;
import io.github.easyretrofit.spring.boot.test.single.api.HelloApiV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author liuziyuan
 */
@Slf4j
@RestController
@RequestMapping("/v1/hello")
public class HelloController {

    @Autowired
    private HelloApi helloApi;

    @Autowired
    private HelloApiV2 helloApiV2;

    @GetMapping("/{message}")
    public ResponseEntity<String> hello(@PathVariable String message) throws IOException {
        final String body = helloApi.hello(message).execute().body().getMessage();
        final String bodyV2 = helloApiV2.hello(message).execute().body().getMessage();
        return ResponseEntity.ok(body + "-" + bodyV2);
    }
}
