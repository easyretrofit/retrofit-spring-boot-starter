package io.github.liuziyuan.retrofit.extension.sentinel.core.util;

import okhttp3.Request;
import retrofit2.Invocation;

import java.lang.reflect.Method;
import java.util.Objects;

public class ResourceNameUtil {

    public static String getConventionResourceName(Request request) {
        Method method = Objects.requireNonNull(request.tag(Invocation.class)).method();
        String className = method.getDeclaringClass().getName();
        return className + "." + method.getName();
    }
}
