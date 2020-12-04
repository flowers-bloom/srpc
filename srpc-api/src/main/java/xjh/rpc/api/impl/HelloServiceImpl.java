package xjh.rpc.api.impl;


import xjh.rpc.api.GreetService;

/**
 * @author XJH
 * @date 2020/11/26
 */
public class HelloServiceImpl implements GreetService {
    @Override
    public String sayHello(String s) {
        return "hello, " + s;
    }
}
