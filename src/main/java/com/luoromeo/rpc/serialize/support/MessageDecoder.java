package com.luoromeo.rpc.serialize.support;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @description RPC消息解码接口
 * @author zhanghua.luo
 * @date 2018年03月30日 13:51
 * @modified By
 */
public class MessageDecoder extends ByteToMessageDecoder {

    public static final int MESSAGE_LENGTH = MessageCodecUtil.MESSAGE_LENGTH;

    private MessageCodecUtil util;

    public MessageDecoder(MessageCodecUtil util) {
        this.util = util;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 出现粘包导致消息长度不对，直接返回
        if (in.readableBytes() < MessageDecoder.MESSAGE_LENGTH) {
            return;
        }

        in.markReaderIndex();

        // 读取消息内容长度
        int messageLength = in.readInt();

        if (messageLength < 0) {
            ctx.close();
        }

        // 读到的消息长度和报文头的已知长度不匹配。那就重置一下ByteBuf读索引的位置
        if (in.readableBytes() < messageLength) {
            in.resetReaderIndex();
        } else {
            byte[] messageBody = new byte[messageLength];
            in.readBytes(messageBody);

            try {
                Object obj = util.decode(messageBody);
                out.add(obj);
            } catch (IOException ex) {
                Logger.getLogger(MessageDecoder.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
