package xjh.rpc.transport.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.transport.client.NettyClient;
import xjh.rpc.transport.client.RetryPolicy;

import java.util.concurrent.TimeUnit;

/**
 * @Author XJH
 * @Date 2020/11/12
 * @Description
 */
@Slf4j
public class ReconnectionHandler extends ChannelInboundHandlerAdapter {
    private NettyClient client;
    private RetryPolicy retryPolicy;

    public ReconnectionHandler(NettyClient client) {
        this.client = client;
        this.retryPolicy = client.getRetryPolicy();
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
        if (retryPolicy.getRetryCount() == 0) {
            log.warn("disconnect from the remote {}", client.getEndpoint());
            ctx.close();
        }

        boolean allowRetry = retryPolicy.allowRetry();
        if (allowRetry) {
            int second = retryPolicy.getWaitSecond();
            log.info("reconnection after {}s for the {} times", second,
                    retryPolicy.getRetryCount());

            ctx.executor().schedule(() -> {
                log.info("trying to reconnect remote {}", client.getEndpoint());
                client.connect();
            }, second, TimeUnit.SECONDS);

            retryPolicy.retryCountIncr();
        }else {
            log.info("reconnect remote {} failed! client close.", client.getEndpoint());
            client.close();
        }

        ctx.fireChannelInactive();
    }
}
