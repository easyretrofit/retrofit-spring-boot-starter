package io.github.liuziyuan.retrofit.extension.sentinel.core.util;

import okhttp3.Request;
import retrofit2.Invocation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class ResourceNameUtil {

    public static String getConventionResourceName(Request request) {
        Invocation tag = request.tag(Invocation.class);
        Method method = Objects.requireNonNull(tag).method();
        String className = method.getDeclaringClass().getName();
        return className + "." + method.getName();
    }
}
