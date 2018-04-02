package com.luoromeo.rpc.serialize.support;

import io.netty.channel.ChannelPipeline;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年03月30日 11:54
 * @modified By
 */
public interface RpcSerializeFrame {

    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}
