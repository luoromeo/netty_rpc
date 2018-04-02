package com.luoromeo.rpc.serialize.support.kryo;

import com.luoromeo.rpc.serialize.support.MessageCodecUtil;
import com.luoromeo.rpc.serialize.support.MessageDecoder;

/**
 * @description Kryo解码器
 * @author zhanghua.luo
 * @date 2018年03月30日 15:12
 * @modified By
 */
public class KryoDecoder extends MessageDecoder {
    public KryoDecoder(MessageCodecUtil util) {
        super(util);
    }
}
