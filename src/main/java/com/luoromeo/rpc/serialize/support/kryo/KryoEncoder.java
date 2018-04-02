package com.luoromeo.rpc.serialize.support.kryo;

import com.luoromeo.rpc.serialize.support.MessageCodecUtil;
import com.luoromeo.rpc.serialize.support.MessageDecoder;

/**
 * @description Kryo编码器
 * @author zhanghua.luo
 * @date 2018年03月30日 15:13
 * @modified By
 */
public class KryoEncoder extends MessageDecoder {

    public KryoEncoder(MessageCodecUtil util) {
        super(util);
    }
}
