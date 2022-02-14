package io.github.liuziyuan.retrofit.demo;

import io.github.liuziyuan.retrofit.demo.api.TestInheritApi;
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

    @SneakyThrows
    public String test() {
        final Call<String> test = testInheritApi.test1();
        return test.execute().body();
    }
}
