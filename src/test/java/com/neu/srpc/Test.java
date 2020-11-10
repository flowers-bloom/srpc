package com.neu.srpc;

import com.neu.srpc.codec.FastJsonImpl;
import com.neu.srpc.codec.Serializer;

/**
 * @Author XJH
 * @Date 2020/11/09
 * @Description
 */
public class Test {
    public static void main(String[] args) {
        String s = "xiao 123";
        Serializer serializer = new FastJsonImpl();

        byte[] bytes = serializer.deserialize(s);
        System.out.println(bytes.length);
    }
}
