package com.neu.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
@Data
@AllArgsConstructor
public class Endpoint {
    private String ip;
    private int port;
}
