package io.github.easyretrofit.spring.boot.test.single;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class SingleInstanceApplicationTest {

    @Resource
    private RetrofitResourceContext retrofitResourceContext;

    @Test
    public void mainTest() {
        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
        assertEquals(retrofitClients.size(), 1);
        assertEquals(retrofitClients.get(0).getRetrofitApiInterfaceBeans().size(), 2);
    }
}