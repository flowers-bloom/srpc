package client;

import xjh.rpc.api.GreetService;
import xjh.rpc.api.PersonService;
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

        GreetService greetService = consumer.getProxy(GreetService.class);
        System.out.println(greetService.sayHello("greetService"));

        PersonService personService = consumer.getProxy(PersonService.class);
        personService.say();

        /*
        2. xml 配置文件调用 暂无法使用
         */
//        ClassPathXmlApplicationContext context =
//                new ClassPathXmlApplicationContext("consumer.xml");
//        context.start();
//
//        GreetService greetService = (GreetService) context.getBean("greetService");
//        String hello = greetService.sayHello("i am consumer");
//        System.out.println(hello);
    }
}
