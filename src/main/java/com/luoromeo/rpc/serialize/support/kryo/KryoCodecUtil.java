package com.luoromeo.rpc.serialize.support.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import com.luoromeo.rpc.serialize.support.MessageCodecUtil;

import io.netty.buffer.ByteBuf;

/**
 * @description Kryo编解码工具类
 * @author zhanghua.luo
 * @date 2018年03月30日 14:40
 * @modified By
 */
public class KryoCodecUtil implements MessageCodecUtil {

    private KryoPool pool;

    private static Closer closer = Closer.create();

    public KryoCodecUtil(KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            KryoSerialize kryoSerialize = new KryoSerialize(pool);
            kryoSerialize.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
        } finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            KryoSerialize kryoSerialize = new KryoSerialize(pool);
            return kryoSerialize.deserialize(byteArrayInputStream);
        } finally {
            closer.close();
        }
    }
}
