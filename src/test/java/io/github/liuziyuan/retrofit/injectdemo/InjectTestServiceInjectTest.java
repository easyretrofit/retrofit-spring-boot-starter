package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.BaseTest;
import io.github.liuziyuan.retrofit.injectdemo.api.InjectTestApi;
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
class InjectTestServiceInjectTest extends BaseTest {

    @InjectMocks
    private InjectTestService injectTestService;

    @Mock
    private InjectTestApi injectTestApi;

    private MockInjectTestApi mockTestApi;

    @BeforeEach
    void setUp() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost").build();
        // Create a MockRetrofit object with a NetworkBehavior which manages the fake behavior of calls.
        NetworkBehavior behavior = NetworkBehavior.create();
        MockRetrofit mockRetrofit =
                new MockRetrofit.Builder(retrofit).networkBehavior(behavior).build();
        BehaviorDelegate<InjectTestApi> delegate = mockRetrofit.create(InjectTestApi.class);
        mockTestApi = new MockInjectTestApi(delegate);
    }

    @SneakyThrows
    @Test
    void test1() {

        final Call<String> call = mockTestApi.test1();
        Mockito.when(injectTestApi.test1()).thenReturn(call);
        final String test = injectTestService.test();
        Assert.assertEquals(test, "hello");
    }

    class MockInjectTestApi implements InjectTestApi {
        private BehaviorDelegate<InjectTestApi> delegate;

        public MockInjectTestApi(BehaviorDelegate<InjectTestApi> delegate) {
            this.delegate = delegate;
        }

        @Override
        public Call<String> test1() {
            String result = "hello";
            return delegate.returningResponse(result).test1();
        }
    }
}