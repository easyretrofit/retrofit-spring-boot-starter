package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBeanGenerator;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBeanGenerator;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * the builder of Retrofit resource context, used to assemble all the retrofit resource
 *
 * @author liuziyuan
 */
public class RetrofitResourceContextBuilder {

    private List<RetrofitClientBean> retrofitClientBeanList;

    private Map<String, RetrofitServiceBean> retrofitServiceBeanHashMap;
    private final Environment environment;

    public RetrofitResourceContextBuilder(Environment environment) {
        retrofitClientBeanList = new ArrayList<>();
        retrofitServiceBeanHashMap = new HashMap<>();
        this.environment = environment;
    }

    public void build(Set<Class<?>> retrofitBuilderClassSet) {
        List<RetrofitServiceBean> retrofitServiceBeanList = setRetrofitServiceBeanList(retrofitBuilderClassSet);
        retrofitClientBeanList = setRetrofitClientBeanList(retrofitServiceBeanList);
    }

    public List<RetrofitClientBean> getRetrofitClientBeanList() {
        return retrofitClientBeanList;
    }


    private List<RetrofitServiceBean> setRetrofitServiceBeanList(Set<Class<?>> retrofitBuilderClassSet) {
        List<RetrofitServiceBean> retrofitServiceBeanList = new ArrayList<>();
        RetrofitServiceBeanGenerator serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitServiceBeanGenerator(clazz, environment);
            final RetrofitServiceBean serviceBean = serviceBeanHandler.generate();
            retrofitServiceBeanList.add(serviceBean);
        }
        return retrofitServiceBeanList;
    }

    private List<RetrofitClientBean> setRetrofitClientBeanList(List<RetrofitServiceBean> serviceBeanList) {
        List<RetrofitClientBean> clientBeanList = new ArrayList<>();
        RetrofitClientBeanGenerator clientBeanHandler;
        for (RetrofitServiceBean serviceBean : serviceBeanList) {
            clientBeanHandler = new RetrofitClientBeanGenerator(clientBeanList, serviceBean);
            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
            if (retrofitClientBean != null && clientBeanList.stream().noneMatch(clientBean -> clientBean.getRetrofitInstanceName().equals(retrofitClientBean.getRetrofitInstanceName()))) {
                clientBeanList.add(retrofitClientBean);
            }
        }
        return clientBeanList;
    }

    public Map<String, RetrofitServiceBean> getRetrofitServiceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitServiceBean retrofitService : retrofitClient.getRetrofitServices()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
            }
        }
        return retrofitServiceBeanHashMap;
    }


}
