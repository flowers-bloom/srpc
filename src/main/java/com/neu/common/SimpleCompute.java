package com.neu.common;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description 测试 rpc 的实现类
 */
public class SimpleCompute implements Compute {
    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int minus(int a, int b) {
        return a-b;
    }
}
