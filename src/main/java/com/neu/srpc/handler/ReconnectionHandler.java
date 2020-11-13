package com.neu.srpc.handler;

import com.neu.client.RetryPolicy;
import com.neu.client.RpcClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author XJH
 * @Date 2020/11/12
 * @Description
 */
@Slf4j
public class ReconnectionHandler extends ChannelInboundHandlerAdapter {
    private RpcClient rpcClient;
    private RetryPolicy retryPolicy;

    public ReconnectionHandler(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        this.retryPolicy = rpcClient.getRetryPolicy();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接建立后，重置重连次数变量
        log.info("reset retry count");
        retryPolicy.resetRetryCount();
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        log.info("ReconnectionHandler hashcode = {}", this.hashCode());

        if (retryPolicy.getRetryCount() == 0) {
            log.warn("disconnect from the remote {}", rpcClient.getEndpoint());
            ctx.close();
        }

        boolean allowRetry = retryPolicy.allowRetry();
        if (allowRetry) {
            int second = retryPolicy.getWaitSecond();
            log.info("reconnection after {}s for the {} times", second,
                    retryPolicy.getRetryCount());

            ctx.executor().schedule(() -> {
                log.info("trying to reconnect remote {}", rpcClient.getEndpoint());
                rpcClient.connect();
            }, second, TimeUnit.SECONDS);

            retryPolicy.retryCountIncr();
        }else {
            log.info("reconnect remote {} failed! client close.", rpcClient.getEndpoint());
            rpcClient.close();
        }

        ctx.fireChannelInactive();
    }
}
