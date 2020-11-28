package xjh.rpc.core.common;

import xjh.rpc.transport.protocol.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author XJH
 * @Date 2020/11/09
 * @Description
 */
public class Constant {

    /**
     * 存储（RequestId，CompletableFuture）元组
     * 用于实现线程间通信
     */
    public static final Map<Long, SyncFuture<Response>> ID_FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 存储（接口类，实现类）元组
     * 用于获取接口的实现类
     */
    public static final Map<Class, Class> SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * zk 实例化参数
     */
    public static final String ZK_ROOT_SERVICE = "/srpc";
    public static final String ZK_ADDR = "127.0.0.1:2181";
    public static final int ZK_SESSION_TIMEOUT = 5000;
}
