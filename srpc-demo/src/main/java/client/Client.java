package client;

import xjh.rpc.api.HelloService;
import xjh.rpc.core.cluster.ConsistentHashLoadBalance;
import xjh.rpc.core.consumer.Consumer;
import xjh.rpc.transport.common.Endpoint;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
public class Client {

    public static void main(String[] args) {
        /*
        1. api 直接调用
         */
        Consumer consumer = new Consumer();
        Endpoint remoteAddress = new Endpoint("127.0.0.1:9004");

        consumer//.registry("127.0.0.1:2181")
                .remoteAddress(remoteAddress)
                .name("test1")
                .loadBalance(new ConsistentHashLoadBalance())
                .build();

        HelloService helloService = consumer.getProxy(HelloService.class);
        String hello = helloService.sayHello("i am consumer.");
        System.out.println(hello);

        /*
        2. xml 配置文件调用 暂无法使用
         */
//        ClassPathXmlApplicationContext context =
//                new ClassPathXmlApplicationContext("consumer.xml");
//        context.start();
//
//        HelloService helloService = (HelloService) context.getBean("helloService");
//        String hello = helloService.sayHello("i am consumer");
//        System.out.println(hello);
    }
}
