package io.github.liuziyuan.retrofit.extension.sentinel.core.util;

import okhttp3.Request;
import retrofit2.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResourceNameUtil {

    public static String getConventionResourceName(Request request) {
        Invocation tag = request.tag(Invocation.class);
        Method method = Objects.requireNonNull(tag).method();
        return getConventionResourceName(method);
    }

    public static String getConventionResourceName(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        Arrays.stream(method.getParameters()).sequential().forEach(parameterType -> {
            sb.append("_");
            sb.append(parameterType.getParameterizedType().getTypeName());
        });

        return sb.toString();
    }
}
