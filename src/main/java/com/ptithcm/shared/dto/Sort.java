package com.ptithcm.shared.dto;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class Sort {

    @FunctionalInterface
    public interface PropertyFunc<T, R> extends java.io.Serializable {
        R apply(T t);
    }

    public static <T, R> String asc(PropertyFunc<T, R> fn) {
        return getPropertyName(fn);
    }

    public static <T, R> String desc(PropertyFunc<T, R> fn) {
        return "-" + getPropertyName(fn);
    }

    public static String asc(String field) {
        return field;
    }

    public static String desc(String field) {
        return "-" + field;
    }

    private static <T, R> String getPropertyName(PropertyFunc<T, R> fn) {
        try {
            Method writeReplace = fn.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) writeReplace.invoke(fn);
            String implMethodName = lambda.getImplMethodName();
            String prefix = "";
            if (implMethodName.startsWith("get")) {
                prefix = "get";
            } else if (implMethodName.startsWith("is")) {
                prefix = "is";
            } else {
                return implMethodName;
            }
            String propertyName = implMethodName.substring(prefix.length());
            return Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
        } catch (Exception e) {
            throw new RuntimeException("Không thể trích xuất tên thuộc tính từ method reference", e);
        }
    }
}
