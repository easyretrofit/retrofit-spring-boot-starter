package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import com.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuziyuan
 * @date 12/31/2021 3:34 PM
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public final class RetrofitResourceContext {

    private Environment environment;
    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;
    private List<RetrofitClientBean> retrofitClients;

    public RetrofitResourceContext() {
        retrofitClients = new ArrayList<>();
    }

    void setRetrofitClients(List<RetrofitClientBean> retrofitClients) {
        this.retrofitClients = retrofitClients;
    }

    List<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

    public RetrofitServiceBean getRetrofitServiceBean(String currentClass) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitServiceBean retrofitService : retrofitClient.getRetrofitServices()) {
                if (retrofitService.getSelfClazz().getName().equals(currentClass)) {
                    return retrofitService;
                }
            }
        }
        return null;
    }

}
