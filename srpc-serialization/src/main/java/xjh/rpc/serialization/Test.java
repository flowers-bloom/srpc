package xjh.rpc.serialization;

import xjh.rpc.serialization.impl.fastjson.FastJsonImpl;
import xjh.rpc.serialization.impl.hessian.HessianImpl;
import xjh.rpc.serialization.impl.kryo.KryoImpl;

import java.util.Arrays;

/**
 * @author XJH
 * @date 2020/11/28
 */
public class Test {

    public static void main(String[] args) {
        String s = "hello,123,!@#";

        Serialization fastjson = new FastJsonImpl();

        System.out.println("----------fastjson----------");
        byte[] bytes1 = fastjson.deserialize(s);
        System.out.println(Arrays.toString(bytes1));

        String serialize1 = fastjson.serialize(bytes1, String.class);
        System.out.println(serialize1);
        System.out.println();

        Serialization hessian = new HessianImpl();
        System.out.println("----------hessian----------");

        byte[] bytes2 = hessian.deserialize(s);
        System.out.println(Arrays.toString(bytes2));

        String serialize2 = hessian.serialize(bytes2, String.class);
        System.out.println(serialize2);
        System.out.println();

        Serialization kryo = new KryoImpl();
        System.out.println("----------kryo----------");

        byte[] bytes3 = kryo.deserialize(s);
        System.out.println(Arrays.toString(bytes3));

        String serialize3 = kryo.serialize(bytes3, String.class);
        System.out.println(serialize3);
        System.out.println();
    }
}
