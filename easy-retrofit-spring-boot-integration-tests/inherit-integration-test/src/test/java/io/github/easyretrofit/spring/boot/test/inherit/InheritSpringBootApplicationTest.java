package io.github.easyretrofit.spring.boot.test.inherit;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InheritSpringBootApplicationTest {

    @Resource
    private RetrofitResourceContext retrofitResourceContext;

    @Test
    public void mainTest() {
        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiInterfaceBean RetrofitApiInterfaceBean : retrofitClient.getRetrofitApiInterfaceBeans()) {
                if (RetrofitApiInterfaceBean.getSelfClazz().equals(MixedApi.class)) {
                    assertEquals(RetrofitApiInterfaceBean.getParentClazz(), BaseApi.class);
                }
                if (RetrofitApiInterfaceBean.getSelfClazz().equals(G2L3Api.class)) {
                    assertTrue(RetrofitApiInterfaceBean.getSelf2ParentClasses().contains(G2L2Api.class));
                    assertTrue(RetrofitApiInterfaceBean.getSelf2ParentClasses().contains(G2L1Api.class));
                    assertTrue(RetrofitApiInterfaceBean.getSelf2ParentClasses().contains(BaseApi.class));
                }
            }
        }
        assertEquals(retrofitClients.size(), 3);
    }
}