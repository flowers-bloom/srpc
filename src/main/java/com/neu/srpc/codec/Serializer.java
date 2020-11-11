package com.neu.srpc.codec;

/**
 * @Author XJH
 */
public interface Serializer {
    /**
     * 将字节数组序列化成特定对象
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T serialize(byte[] bytes, Class<T> clazz);

    /**
     * 将对象反序列化成字节数组
     *
     * @param object
     * @return
     */
    byte[] deserialize(Object object);
}
