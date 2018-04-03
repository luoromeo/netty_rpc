package com.luoromeo.rpc.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @description 服务应答结构
 * @author zhanghua.luo
 * @date 2018年03月28日 23:25
 * @modified By
 */
@Getter
@Setter
public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String error;

    private Object resultDesc;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(Object resultDesc) {
        this.resultDesc = resultDesc;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
