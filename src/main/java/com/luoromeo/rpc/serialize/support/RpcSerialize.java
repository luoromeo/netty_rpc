package com.luoromeo.rpc.serialize.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年03月30日 11:05
 * @modified By
 */
public interface RpcSerialize {

    /**
     * @description 序列化
     * @author zhanghua.luo
     * @date 2018年03月30日 11:06:52
     * @param outputStream
     * @param o
     * @return
     */
    void serialize(OutputStream outputStream, Object o) throws IOException;

    /**
     * @description 反序列化
     * @author zhanghua.luo
     * @date 2018年03月30日 11:07:04
     * @param inputStream
     * @return
     */
    Object deserialize(InputStream inputStream) throws IOException;
}
