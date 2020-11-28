package xjh.rpc.core.consumer;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.core.common.Constant;
import xjh.rpc.core.common.SyncFuture;
import xjh.rpc.transport.handler.AbstractResponseHandler;
import xjh.rpc.transport.protocol.Response;

/**
 * @Author XJH
 * @Date 2020/11/10
 * @Description 客户端存根
 *
 */
@Slf4j
public class ResponseHandler extends AbstractResponseHandler {

    @Override
    public void process(ChannelHandlerContext ctx, Response resp) {
        SyncFuture<Response> future = Constant.ID_FUTURE_MAP.get(resp.getRequestId());
        future.complete(resp);
    }
}
