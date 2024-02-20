package io.github.liuziyuan.retrofit.springboot;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBeanGenerator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitServiceBeanGenerator;

import java.util.*;

/**
 * the builder of Retrofit resource context, used to assemble all the retrofit resource
 *
 * @author liuziyuan
 */
public class RetrofitResourceContextBuilder {

    private List<RetrofitClientBean> retrofitClientBeanList;
    private List<RetrofitServiceBean> retrofitServiceBeanList;
    private final Map<String, RetrofitServiceBean> retrofitServiceBeanHashMap;
    private final Env env;

    public RetrofitResourceContextBuilder(Env env) {
        retrofitClientBeanList = new ArrayList<>();
        retrofitServiceBeanList = new ArrayList<>();
        retrofitServiceBeanHashMap = new HashMap<>();
        this.env = env;
    }

    public RetrofitResourceContextBuilder build(Set<Class<?>> retrofitBuilderClassSet) {
        setRetrofitServiceBeanList(retrofitBuilderClassSet);
        setRetrofitClientBeanList();
        setRetrofitServiceBeanHashMap();
        return this;
    }

    public List<RetrofitClientBean> getRetrofitClientBeanList() {
        return retrofitClientBeanList;
    }

    public Map<String, RetrofitServiceBean> getRetrofitServiceBeanHashMap() {
        return retrofitServiceBeanHashMap;
    }

    public List<RetrofitServiceBean> getRetrofitServiceBean() {
        return retrofitServiceBeanList;
    }

    private void setRetrofitServiceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitServiceBean retrofitService : retrofitClient.getRetrofitServices()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
            }
        }
    }

    private void setRetrofitServiceBeanList(Set<Class<?>> retrofitBuilderClassSet) {
        RetrofitServiceBeanGenerator serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitServiceBeanGenerator(clazz, env);
            final RetrofitServiceBean serviceBean = serviceBeanHandler.generate();
            if (serviceBean != null) {
                retrofitServiceBeanList.add(serviceBean);
            }
        }
    }

    private void setRetrofitClientBeanList() {
        RetrofitClientBeanGenerator clientBeanHandler;
        for (RetrofitServiceBean serviceBean : getRetrofitServiceBean()) {
            clientBeanHandler = new RetrofitClientBeanGenerator(retrofitClientBeanList, serviceBean);
            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
            if (retrofitClientBean != null && retrofitClientBeanList.stream().noneMatch(clientBean -> clientBean.getRetrofitInstanceName().equals(retrofitClientBean.getRetrofitInstanceName()))) {
                retrofitClientBeanList.add(retrofitClientBean);
            }
        }
    }


}
