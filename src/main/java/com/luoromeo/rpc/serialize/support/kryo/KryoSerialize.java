package com.luoromeo.rpc.serialize.support.kryo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.luoromeo.rpc.serialize.support.RpcSerialize;

/**
 * @description Kryo序列化/反序列化实现
 * @author zhanghua.luo
 * @date 2018年03月30日 14:24
 * @modified By
 */
public class KryoSerialize implements RpcSerialize {

    private KryoPool pool;

    public KryoSerialize(final KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void serialize(OutputStream outputStream, Object o) throws IOException {
        Kryo kryo = pool.borrow();
        Output out = new Output(outputStream);
        kryo.writeClassAndObject(out, o);
        out.close();
        pool.release(kryo);
    }

    @Override
    public Object deserialize(InputStream inputStream) throws IOException {
        Kryo kryo = pool.borrow();
        Input in = new Input(inputStream);
        Object result = kryo.readClassAndObject(in);
        in.close();
        pool.release(kryo);
        return result;
    }
}
