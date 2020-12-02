package xjh.rpc.api.impl;

import xjh.rpc.api.HelloService;

/**
 * @author XJH
 * @date 2020/12/01
 */
public class HelloServiceImpl2 implements HelloService {
    public String sayHello(String s) {
        return "hi, " + s;
    }
}
