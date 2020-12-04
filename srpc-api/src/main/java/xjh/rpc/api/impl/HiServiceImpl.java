package xjh.rpc.api.impl;

import xjh.rpc.api.GreetService;

/**
 * @author XJH
 * @date 2020/12/01
 */
public class HiServiceImpl implements GreetService {
    @Override
    public String sayHello(String s) {
        return "hi, " + s;
    }
}
