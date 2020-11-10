package com.neu.srpc.registry;

import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description
 */
public abstract class LoadBalance {
    public static List<String> SERVICE_LIST;

    public abstract String select();
}
