package com.neu.srpc.codec;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description
 */
public class FastJsonImpl implements Serializer {
    public static Serializer serializer = new FastJsonImpl();

    @Override
    public <T> T serialize(byte[] bytes, Class<T> clazz) {
        return JSONObject.parseObject(bytes, clazz);
    }

    @Override
    public byte[] deserialize(Object object) {
        return JSONObject.toJSONBytes(object);
    }
}
