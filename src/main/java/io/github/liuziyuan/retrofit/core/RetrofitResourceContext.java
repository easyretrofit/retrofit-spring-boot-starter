package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitServiceBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The top structure contains all the objects created in the starter
 *
 * @author liuziyuan
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class RetrofitResourceContext {

    private List<RetrofitClientBean> retrofitClients;
    private Map<String, RetrofitServiceBean> retrofitServices;

    public RetrofitResourceContext(List<RetrofitClientBean> retrofitClients, Map<String, RetrofitServiceBean> retrofitServices) {
        this.retrofitServices = retrofitServices;
        this.retrofitClients = retrofitClients;
    }

    public void setRetrofitClients(List<RetrofitClientBean> retrofitClients) {
        this.retrofitClients = retrofitClients;
    }

    public List<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

    public RetrofitServiceBean getRetrofitServiceBean(String currentClass) {
        return retrofitServices.get(currentClass);
    }

}
