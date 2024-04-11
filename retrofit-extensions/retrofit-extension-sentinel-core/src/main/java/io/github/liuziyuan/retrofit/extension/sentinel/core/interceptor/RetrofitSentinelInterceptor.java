package io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.Rule;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelResource;
import io.github.liuziyuan.retrofit.extension.sentinel.core.util.ResourceNameUtil;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Method;

public class RetrofitSentinelInterceptor extends BaseInterceptor {

    private final RetrofitResourceContext context;

    public RetrofitSentinelInterceptor(RetrofitResourceContext context) {
        this.context = context;
    }

    @Override
    protected Response executeIntercept(Chain chain) throws IOException {
        Entry entry = null;
        Request request = chain.request();
        final Method method = super.getRequestMethod(request);
        final String clazzName = super.getClazzNameByMethod(method);
        final RetrofitApiServiceBean currentServiceBean = context.getRetrofitApiServiceBean(clazzName);
        RetrofitSentinelResource retrofitSentinelResource = currentServiceBean.getAnnotationResource(RetrofitSentinelResource.class);
        if (retrofitSentinelResource != null) {
            try {
                String name = ResourceNameUtil.getConventionResourceName(request);
                entry = SphU.entry(name, retrofitSentinelResource.resourceType(), retrofitSentinelResource.entryType());
                return chain.proceed(request);
            } catch (BlockException e) {
                // 资源访问阻止，被限流或被降级
                throw new SentinelBlockException(e, currentServiceBean, request);
            } catch (IOException ex) {
                // 若需要配置降级规则，需要通过这种方式记录业务异常
                Tracer.traceEntry(ex, entry);
                throw ex;
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
