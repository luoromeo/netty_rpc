package com.luoromeo.rpc.serialize.support.hessian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.luoromeo.rpc.serialize.support.RpcSerialize;

/**
 * @description Hessian序列化/反序列化实现
 * @author zhanghua.luo
 * @date 2018年03月30日 15:15
 * @modified By
 */
public class HessianSerialize implements RpcSerialize {

    @Override
    public void serialize(OutputStream outputStream, Object o) throws IOException {

        Hessian2Output ho = new Hessian2Output(outputStream);

        try {
            ho.startMessage();
            ho.writeObject(o);
            ho.completeMessage();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            ho.close();
        }

    }

    @Override
    public Object deserialize(InputStream inputStream) throws IOException {

        Object result = null;
        Hessian2Input hi = new Hessian2Input(inputStream);

        try {
            hi.startMessage();
            result = hi.readObject();
            hi.completeMessage();
            hi.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            hi.close();
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result;
    }
}
