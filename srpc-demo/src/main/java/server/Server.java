package server;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import xjh.rpc.core.provider.Provider;
import xjh.rpc.transport.common.Endpoint;

/**
 * @author XJH
 * @date 2020/11/08
 */
public class Server {
    public static void main(String[] args) {
        /*
        1. api 直接调用
         */
        Endpoint endpoint = new Endpoint("127.0.0.1:9004");

        Provider provider = new Provider();
        provider.address(endpoint)
                .serviceName("test4")
                .weight(6)
                .registry("127.0.0.1:2181")
                .build();

        /*
        2. xml 配置文件调用
         */

//        ClassPathXmlApplicationContext context =
//                new ClassPathXmlApplicationContext("META-INF/spring/provider.xml");
//        context.start();
    }
}
