package io.github.liuziyuan.test.retrofit.spring.boot.quickstart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private TestApi testApi;

    @GetMapping("hello/message")
    public String test() throws IOException {
        return testApi.getMessage().execute().message();
    }
}
