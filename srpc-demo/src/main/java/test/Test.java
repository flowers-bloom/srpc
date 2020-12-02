package test;

import xjh.rpc.api.HelloService;
import xjh.rpc.common.spi.ExtensionLoader;

/**
 * @author XJH
 * @date 2020/12/02
 */
public class Test {
    public static void main(String[] args) {
        ExtensionLoader<HelloService> loader = ExtensionLoader.load(HelloService.class);
        HelloService helloService = loader.getExtension("hi");
        String hello = helloService.sayHello("ExtensionLoader");
        System.out.println(hello);
    }
}
