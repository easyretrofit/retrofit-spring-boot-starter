package io.github.liuziyuan.retrofit.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static Object getMethodReturnValue(Object object, String methodName) {

        try {
            Method method = object.getClass().getDeclaredMethod(methodName);
            return method.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setMethodValue(Object object, String methodName, Object value) {
        try {
            Method method = object.getClass().getDeclaredMethod(methodName);
            method.invoke(object, value);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {

        }
    }
}
