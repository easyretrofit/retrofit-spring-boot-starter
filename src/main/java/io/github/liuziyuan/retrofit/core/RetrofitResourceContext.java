package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
    private Map<String, RetrofitApiServiceBean> retrofitServices;

    public RetrofitResourceContext(List<RetrofitClientBean> retrofitClients, Map<String, RetrofitApiServiceBean> retrofitServices) {
        this.retrofitServices = retrofitServices;
        this.retrofitClients = retrofitClients;
    }

    public List<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

    public RetrofitApiServiceBean getRetrofitServiceBean(String currentClass) {
        return retrofitServices.get(currentClass);
    }

}
