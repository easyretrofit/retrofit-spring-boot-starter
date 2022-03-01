package io.github.liuziyuan.retrofit.extension;

import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import retrofit2.Invocation;
import retrofit2.internal.EverythingIsNonNull;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Abstract class of Interceptor, The custom interceptor needs to inherit it
 *
 * @author liuziyuan
 */
public abstract class BaseInterceptor implements Interceptor {

    protected RetrofitResourceContext context;
    private String[] include;
    private String[] exclude;
    private PathMatcher pathMatcher = new AntPathMatcher();


    protected BaseInterceptor() {
    }

    protected BaseInterceptor(RetrofitResourceContext context) {
        this.context = context;
    }

    @SneakyThrows
    @Override
    @EverythingIsNonNull
    public final Response intercept(Chain chain) {
        Request request = chain.request();
        String path = request.url().encodedPath();
        if (exclude != null && isMatch(exclude, path)) {
            return chain.proceed(request);
        }
        if (include != null && !isMatch(include, path)) {
            return chain.proceed(request);
        }
        return executeIntercept(chain);
    }

    /**
     * execute intercept for OKHttpClient Interceptor
     *
     * @param chain Chain
     * @return Response
     */
    protected abstract Response executeIntercept(Chain chain);

    private boolean isMatch(String[] patterns, String path) {
        if (patterns == null || patterns.length == 0) {
            return false;
        }
        for (String pattern : patterns) {
            boolean match = pathMatcher.match(pattern, path);
            if (match) {
                return true;
            }
        }
        return false;
    }


    public void setInclude(String[] include) {
        this.include = include;
    }

    public void setExclude(String[] exclude) {
        this.exclude = exclude;
    }


    protected String getClazzNameByMethod(Method method) {
        return method.getDeclaringClass().getName();
    }

    protected Method getRequestMethod(Request request) {
        return Objects.requireNonNull(request.tag(Invocation.class)).method();
    }
}
