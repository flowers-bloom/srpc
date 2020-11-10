package com.neu.srpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
@Data
public class ServiceInfo {
    private String clazzName;
    private String methodName;
    private String returnType;
    private String[] parameterTypes;

    public ServiceInfo(Class clazz, Method method) {
        this.clazzName = clazz.getName();
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();

        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] args = new String[parameterTypes.length];

        for (int i=0; i<parameterTypes.length; i++) {
            args[i] = parameterTypes[i].getName();
        }

        this.parameterTypes = args;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ServiceInfo that = (ServiceInfo) object;

        return Objects.equals(clazzName, that.clazzName) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(returnType, that.returnType) &&
                Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "clazzName='" + clazzName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
