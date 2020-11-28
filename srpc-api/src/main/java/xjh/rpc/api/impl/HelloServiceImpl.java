package xjh.rpc.api.impl;


import xjh.rpc.api.HelloService;

/**
 * @author XJH
 * @date 2020/11/26
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String s) {
        return "hello, " + s;
    }
}
