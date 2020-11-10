package com.neu.srpc.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description
 */
@Data
public abstract class Packet implements Serializable {
    /**
     * 数据包 class 类型
     * 不进行序列化
     */
    transient Class<? extends Packet> clazz;

    /**
     * 获取类的 class
     * @return
     */
    protected abstract Class<? extends Packet> getClazz();
}
