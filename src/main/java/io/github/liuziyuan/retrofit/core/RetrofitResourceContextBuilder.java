package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.resource.*;

import java.util.*;

/**
 * the builder of Retrofit resource context, used to assemble all the retrofit resource
 *
 * @author liuziyuan
 */
public abstract class RetrofitResourceContextBuilder {

    private List<RetrofitClientBean> retrofitClientBeanList;
    private List<RetrofitApiServiceBean> retrofitApiServiceBeanList;
    private final Map<String, RetrofitApiServiceBean> retrofitServiceBeanHashMap;
    private final Env env;
    private List<io.github.liuziyuan.retrofit.core.Extension> extensions;
    public abstract List<io.github.liuziyuan.retrofit.core.Extension> registerExtension(List<Extension> extensions);

    public RetrofitResourceContextBuilder(Env env) {
        retrofitClientBeanList = new ArrayList<>();
        retrofitApiServiceBeanList = new ArrayList<>();
        retrofitServiceBeanHashMap = new HashMap<>();
        this.env = env;
    }

    public RetrofitResourceContextBuilder build(Set<Class<?>> retrofitBuilderClassSet, RetrofitBuilderBean retrofitBuilderBean) {
        setRetrofitServiceBeanList(retrofitBuilderClassSet, retrofitBuilderBean);
        setRetrofitClientBeanList();
        setRetrofitServiceBeanHashMap();
        return this;
    }

    public List<RetrofitClientBean> getRetrofitClientBeanList() {
        return retrofitClientBeanList;
    }

    public Map<String, RetrofitApiServiceBean> getRetrofitServiceBeanHashMap() {
        return retrofitServiceBeanHashMap;
    }

    public List<RetrofitApiServiceBean> getRetrofitServiceBean() {
        return retrofitApiServiceBeanList;
    }

    private void setRetrofitServiceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitApiServiceBean retrofitService : retrofitClient.getRetrofitServices()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
            }
        }
    }

    private void setRetrofitServiceBeanList(Set<Class<?>> retrofitBuilderClassSet, RetrofitBuilderBean retrofitBuilderBean) {
        extensions = registerExtension(new ArrayList<>());
        RetrofitApiServiceBeanGenerator serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitApiServiceBeanGenerator(clazz, env, retrofitBuilderBean);
            final RetrofitApiServiceBean serviceBean = serviceBeanHandler.generate(extensions);
            if (serviceBean != null) {
                retrofitApiServiceBeanList.add(serviceBean);
            }
        }
    }

    private void setRetrofitClientBeanList() {
        RetrofitClientBeanGenerator clientBeanHandler;
        for (RetrofitApiServiceBean serviceBean : getRetrofitServiceBean()) {
            clientBeanHandler = new RetrofitClientBeanGenerator(retrofitClientBeanList, serviceBean);
            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
            if (retrofitClientBean != null && retrofitClientBeanList.stream().noneMatch(clientBean -> clientBean.getRetrofitInstanceName().equals(retrofitClientBean.getRetrofitInstanceName()))) {
                retrofitClientBeanList.add(retrofitClientBean);
            }
        }
    }

}
