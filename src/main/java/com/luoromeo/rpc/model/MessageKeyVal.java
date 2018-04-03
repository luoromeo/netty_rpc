package com.luoromeo.rpc.model;

import lombok.Data;

import java.util.Map;

/**
 * @description 服务映射容器 RPC服务接口定义、服务接口实现绑定关系容器定义，提供给spring作为容器使用
 * @author zhanghua.luo
 * @date 2018年03月28日 23:38
 * @modified By
 */
@Data
public class MessageKeyVal {

    private Map<String, Object> messageKeyVal;

    public Map<String, Object> getMessageKeyVal() {
        return messageKeyVal;
    }

    public void setMessageKeyVal(Map<String, Object> messageKeyVal) {
        this.messageKeyVal = messageKeyVal;
    }
}
