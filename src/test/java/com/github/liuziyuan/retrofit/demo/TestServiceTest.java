package com.github.liuziyuan.retrofit.demo;

import com.github.liuziyuan.retrofit.BaseTest;
import com.github.liuziyuan.retrofit.demo.api.TestInheritApi;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * @author liuziyuan
 * @date 1/14/2022 6:29 PM
 */
class TestServiceTest extends BaseTest {

    @InjectMocks
    private TestService testService;

    private MockTestService mockTestService;

    @BeforeEach
    void setUp() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost").build();
        // Create a MockRetrofit object with a NetworkBehavior which manages the fake behavior of calls.
        NetworkBehavior behavior = NetworkBehavior.create();
        MockRetrofit mockRetrofit =
                new MockRetrofit.Builder(retrofit).networkBehavior(behavior).build();
        BehaviorDelegate<TestInheritApi> delegate = mockRetrofit.create(TestInheritApi.class);
        mockTestService = new MockTestService(delegate);
    }

    @SneakyThrows
    @Test
    void test1() {

        final Call<String> call = mockTestService.test1();
        final String body = call.execute().body();
        Assert.assertEquals(body, "hello");
    }

    class MockTestService implements TestInheritApi {
        private BehaviorDelegate<TestInheritApi> delegate;

        public MockTestService(BehaviorDelegate<TestInheritApi> delegate) {

        }

        @Override
        public Call<String> test1() {
            String result = "hello";
            return delegate.returningResponse(result).test1();
        }

        @Override
        public Call<String> test() {
            return null;
        }
    }
}