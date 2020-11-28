# SRPC
![build](https://img.shields.io/badge/build-passing-brightgreen) ![oraclejdk](https://img.shields.io/badge/oraclejdk-1.8-red) ![version](https://img.shields.io/badge/version-1.0-blue)  

SRPC 是一个基于 Netty 实现网络通信，Zookeeper 实现服务暴露和发现，并实现了序列化、负载均衡、重连机制的远程调用框架。    

## 特性
1. 自定义声明报文长度的通信协议，解决粘包黏包问题  
2. 基于 Netty 实现高性能网络通信，并增加了客户端重连的容错机制  
3. 使用 Zookeeper 实现服务暴露和发现  
4. 实现的序列化方式：fastjson  
5. 实现的负载均衡算法：随机负载均衡  
6. RPC 提供直连方式和注册中心方式  
7. 接口抽象良好，模块耦合度低  


## 模块
- srpc-serialization 序列化模块  
- srpc-transport 网络通信模块  
- srpc-core 核心模块  
- srpc-api 接口模块  
- srpc-demo 示例模块  


## 功能
项目开始于2020.11.01，以下是目前已实现的功能点  

|        功能点       |  完成时间   |  备注  |
| :----------------: | :--------: | :------------------: | 
| 实现基本的 RPC 通信 | 2020.11.10 | 网络通信接口，使用 Netty 进行实现，并借助 CompletableFuture 实现线程间通信；序列化接口，使用 FastJson 进行实现；远程方法调用，使用 JDK Proxy 类实现动态代理 |
| 实现 Netty 心跳检测及处理；实现 zookeeper 服务注册和发现，以及客户端随机负载均衡 | 2020.11.11 | 使用 Netty 自带心跳检测实现方式；在结点上注册 watcher 实现服务列表的动态更新 |  
| 实现 Netty 断线重连 | 2020.11.13 | 实现自定义有限次数重连策略；实现重连 handler ，采用异步方式连接远程主机，但会造成初始时客户端无法主动发送消息，因为 channel 虽然实例化了，但还未连接上远程主机 |
| 项目拆分成多个模块，实现解耦 | 2020.11.27 | 无 |

## 使用
### 服务提供者
**定义服务接口**  
```java
public interface HelloService {
    String sayHello(String s);
}
```

**在服务提供方实现接口**  
```java
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String s) {
        return "hello, " + s;
    }
}
```

**实现类声明**  
在 resource 目录下创建 META-INF/services 目录，并创建以接口全限定类名为文件名的文件，文件内声明接口的实现类的全限定类名  

eg：
文件名 xjh.rpc.api.HelloService => 内容 xjh.rpc.api.impl.HelloServiceImpl  

**启动服务提供者**  
```java
public class Server {
    public static void main(String[] args) {
        /*
        1. api 直接调用
         */
        Endpoint endpoint = new Endpoint("127.0.0.1:9000");
        new Provider(endpoint, true, "127.0.0.1:2181");

        /*
        2. xml 配置文件调用
         */

//        ClassPathXmlApplicationContext context =
//                new ClassPathXmlApplicationContext("META-INF/spring/provider.xml");
//        context.start();
    }
}
```

### 服务消费者
**调用远程服务**  
```java
public class Client {

    public static void main(String[] args) {
        /*
        1. api 直接调用
         */
        Consumer consumer = new Consumer("127.0.0.1:2181");

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
```