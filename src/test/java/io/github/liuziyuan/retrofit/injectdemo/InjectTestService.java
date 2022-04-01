package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.injectdemo.api.TestApi;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

/**
 * @author liuziyuan
 * @date 1/14/2022 6:27 PM
 */
@Service
public class InjectTestService {
    @Autowired
    private TestApi testApi;

    @SneakyThrows
    public String test() {
        final Call<String> test = testApi.test1();
        return test.execute().body();
    }
}
