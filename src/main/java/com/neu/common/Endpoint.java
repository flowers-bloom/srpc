package com.neu.common;

import lombok.Data;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
@Data
public class Endpoint {
    private String ip;
    private int port;

    public Endpoint(String point) {
        int sep = point.indexOf(':');
        this.ip = point.substring(0, sep);
        this.port = Integer.parseInt(point.substring(sep+1));
    }
}
