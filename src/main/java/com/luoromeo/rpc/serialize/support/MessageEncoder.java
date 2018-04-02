package com.luoromeo.rpc.serialize.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description RPC消息编码接口
 * @author zhanghua.luo
 * @date 2018年03月30日 13:38
 * @modified By
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

    private MessageCodecUtil util = null;

    public MessageEncoder(final MessageCodecUtil util) {
        this.util = util;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
        util.encode(byteBuf, o);
    }
}
