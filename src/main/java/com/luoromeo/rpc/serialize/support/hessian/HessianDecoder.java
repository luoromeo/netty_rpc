package com.luoromeo.rpc.serialize.support.hessian;

import com.luoromeo.rpc.serialize.support.MessageCodecUtil;
import com.luoromeo.rpc.serialize.support.MessageDecoder;

/**
 * @description Hessian解码器
 * @author zhanghua.luo
 * @date 2018年03月30日 17:19
 * @modified By
 */
public class HessianDecoder extends MessageDecoder {
    public HessianDecoder(MessageCodecUtil util) {
        super(util);
    }
}
