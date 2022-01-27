package com.github.liuziyuan.retrofit.demo;

import com.github.liuziyuan.retrofit.demo.api.TestInheritApi;
import com.github.liuziyuan.retrofit.demo.api.TestInheritApi2;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

/**
 * @author liuziyuan
 * @date 1/14/2022 6:27 PM
 */
@Service
public class TestService {
    @Autowired
    private TestInheritApi testInheritApi;
    @Autowired
    private TestInheritApi2 testInheritApi2;

    @SneakyThrows
    public String test() {
        final Call<String> test = testInheritApi.test1();
        final Call<String> test2 = testInheritApi2.test2();
        return test.execute().body();
    }
}
