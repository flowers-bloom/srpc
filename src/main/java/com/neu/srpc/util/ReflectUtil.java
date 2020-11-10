package com.neu.srpc.util;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description 反射工具类
 */
public class ReflectUtil {
    /**
     * 根据类信息实例化出对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取类的 public 方法
     *
     * @param clazz
     * @return
     */
    public static List<Method> getPublicMethod(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pubMethods = new ArrayList<>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                pubMethods.add(method);
            }
        }

        return pubMethods;
    }

    /**
     * 调用指定对象的指定方法
     *
     * @param object
     * @param method
     * @param args
     * @return
     */
    public static Object invoke(Object object, Method method, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
