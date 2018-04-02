package com.luoromeo.rpc.serialize.support;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

/**
 * @description RPC消息编解码借口
 * @author zhanghua.luo
 * @date 2018年03月30日 11:13
 * @modified By
 */
public interface MessageCodecUtil {

    final public static int MESSAGE_LENGTH = 4;

    void encode(final ByteBuf out, final Object message) throws IOException;

    Object decode(byte[] body) throws IOException;

}
