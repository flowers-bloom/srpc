package xjh.rpc.transport.common;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author XJH
 * @date 2020/11/27
 */
public class Constant {
    /**
     * 标识分组是否使用本项目协议
     */
    public static final byte MAGIC_NUM = 36;

    /**
     * netty LengthFieldDecoder 最大消息长度
     */
    public static final int MAX_FRAME_LENGTH = 65535;

    /**
     * netty LengthFieldDecoder 报文中长度字段偏移量
     */
    public static final int LENGTH_FIELD_OFFSET = 2;

    /**
     * netty LengthFieldDecoder 长度字段大小
     */
    public static final int LENGTH_FIELD_LENGTH = 4;

    /**
     * 存储在服务端建立的 Channel
     *
     * 该容器内部实现了 serverChannels 和 noServerChannels 两个 ConcurrentHashMap，
     * 用于分别存储服务端和客户端的 Channel
     */
    public static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
