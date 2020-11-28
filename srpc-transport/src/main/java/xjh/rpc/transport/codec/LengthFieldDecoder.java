package xjh.rpc.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import xjh.rpc.transport.common.Constant;

/**
 * @author XJH
 * @date 2020/11/26
 */
public class LengthFieldDecoder extends LengthFieldBasedFrameDecoder {
    public LengthFieldDecoder() {
        super(Constant.MAX_FRAME_LENGTH, Constant.LENGTH_FIELD_OFFSET,
                Constant.LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getByte(in.readerIndex()) != Constant.MAGIC_NUM) {
            ctx.close();
            return null;
        }

        return super.decode(ctx, in);
    }
}
