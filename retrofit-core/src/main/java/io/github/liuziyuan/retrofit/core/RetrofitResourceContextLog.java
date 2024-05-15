package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitBuilderBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.UrlStatus;
import io.github.liuziyuan.retrofit.core.util.RetrofitUrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RetrofitResourceContextLog {
    private static final Logger log = LoggerFactory.getLogger(RetrofitResourceContextLog.class);
    private final RetrofitResourceContext context;

    public RetrofitResourceContextLog(RetrofitResourceContext context) {
        this.context = context;
    }

    public void showLog() {
        for (RetrofitClientBean retrofitClient : context.getRetrofitClients()) {
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            if (retrofitClient.getUrlStatus().equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                log.warn("---Retrofit Client : HostURL[Dummy]: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            } else {
                log.info("---Retrofit Client : HostURL: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            }
            retrofitClientDebugLog(retrofitClient);
            for (RetrofitApiServiceBean retrofitService : retrofitClient.getRetrofitApiServiceBeans()) {
                final Class<?> selfClazz = retrofitService.getSelfClazz();
                final Class<?> parentClazz = retrofitService.getParentClazz();
                String parentClazzName = null;
                if (!parentClazz.getName().equals(selfClazz.getName())) {
                    parentClazzName = parentClazz.getName();
                }
                log.info("|--API Services: Interface name: {} , Parent Interface name: {}", selfClazz.getName(), parentClazzName);
            }
        }
    }

    private void retrofitClientDebugLog(RetrofitClientBean retrofitClient) {
        final String realHostUrl = retrofitClient.getRealHostUrl();
        RetrofitBuilderBean retrofitBuilder = retrofitClient.getRetrofitBuilder();
        final String globalEnable = retrofitBuilder.isEnable() ? "true" : "false";
        String CallAdapterFactoryString = StringUtils.join(Arrays.stream(retrofitBuilder.getAddCallAdapterFactory()).map(Class::getSimpleName).collect(Collectors.toList()), ",");
        String ConverterFactoryString = StringUtils.join(Arrays.stream(retrofitBuilder.getAddConverterFactory()).map(Class::getSimpleName).collect(Collectors.toList()), ",");
        String callbackExecutorString = retrofitBuilder.getCallbackExecutor().getSimpleName();
        String clientString = retrofitBuilder.getClient().getSimpleName();
        String callFactoryString = retrofitBuilder.getCallFactory().getSimpleName();
        String validateEagerlyString = retrofitBuilder.isValidateEagerly() ? "true" : "false";
        String inheritedInterceptor = retrofitClient.getInheritedInterceptors().toString();
        String interceptor = retrofitClient.getInterceptors().toString();
        log.debug("RetrofitClientBean: HostURL: {}; UrlStatus: {}; globalEnable: {}; CallAdapterFactory: {}; ConverterFactory:{}; callbackExecutor: {}; client: {}; callFactory: {}; validateEagerly: {}; inheritedInterceptor: {}; interceptor: {}",
                realHostUrl, retrofitClient.getUrlStatus(), globalEnable, CallAdapterFactoryString, ConverterFactoryString, callbackExecutorString, clientString, callFactoryString, validateEagerlyString, inheritedInterceptor, interceptor);

    }
}
