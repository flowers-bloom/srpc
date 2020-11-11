package com.neu.cluster;

import com.neu.common.Endpoint;

import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description
 */
public abstract class LoadBalance {
    public static List<Endpoint> SERVICE_LIST;

    public abstract Endpoint select();
}
