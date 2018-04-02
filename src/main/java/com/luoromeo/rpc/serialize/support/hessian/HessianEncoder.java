package com.luoromeo.rpc.serialize.support.hessian;

import com.luoromeo.rpc.serialize.support.MessageCodecUtil;
import com.luoromeo.rpc.serialize.support.MessageEncoder;

/**
 * @description Hessian编码器
 * @author zhanghua.luo
 * @date 2018年03月30日 17:19
 * @modified By
 */
public class HessianEncoder extends MessageEncoder {
    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}
