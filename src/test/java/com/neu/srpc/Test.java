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
//        String point = "123:44";
//
//        int sep = point.indexOf(':');
//        System.out.println(point.substring(sep+1));
        //this.port = Integer.parseInt(point.substring(sep+1));
        int n = 1 << 15;
        System.out.println(n / 1000);
    }
}
