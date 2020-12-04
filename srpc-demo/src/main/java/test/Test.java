package test;

import xjh.rpc.api.GreetService;
import xjh.rpc.common.spi.ExtensionLoader;

/**
 * @author XJH
 * @date 2020/12/02
 */
public class Test {
    public static void main(String[] args) {
        ExtensionLoader<GreetService> loader = ExtensionLoader.load(GreetService.class);
        GreetService greetService = loader.getExtension("hi");
        String hello = greetService.sayHello("ExtensionLoader");
        System.out.println(hello);
    }
}
