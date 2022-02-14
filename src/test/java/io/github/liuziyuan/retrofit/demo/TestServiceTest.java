package io.github.liuziyuan.retrofit.demo;

import io.github.liuziyuan.retrofit.BaseTest;
import io.github.liuziyuan.retrofit.demo.api.TestInheritApi;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Mock
    private TestInheritApi testInheritApi;

    private MockTestApi mockTestApi;

    @BeforeEach
    void setUp() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost").build();
        // Create a MockRetrofit object with a NetworkBehavior which manages the fake behavior of calls.
        NetworkBehavior behavior = NetworkBehavior.create();
        MockRetrofit mockRetrofit =
                new MockRetrofit.Builder(retrofit).networkBehavior(behavior).build();
        BehaviorDelegate<TestInheritApi> delegate = mockRetrofit.create(TestInheritApi.class);
        mockTestApi = new MockTestApi(delegate);
    }

    @SneakyThrows
    @Test
    void test1() {

        final Call<String> call = mockTestApi.test1();
        Mockito.when(testInheritApi.test1()).thenReturn(call);
        final String test = testService.test();
        Assert.assertEquals(test, "hello");
    }

    class MockTestApi implements TestInheritApi {
        private BehaviorDelegate<TestInheritApi> delegate;

        public MockTestApi(BehaviorDelegate<TestInheritApi> delegate) {
            this.delegate = delegate;
        }

        @Override
        public Call<String> test1() {
            String result = "hello";
            return delegate.returningResponse(result).test1();
        }
    }
}