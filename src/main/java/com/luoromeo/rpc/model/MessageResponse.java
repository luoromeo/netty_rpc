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

    private Object result;

    private boolean returnNotNull;

    private long invokeTimespan;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    public long getInvokeTimespan() {
        return invokeTimespan;
    }

    public void setInvokeTimespan(long invokeTimespan) {
        this.invokeTimespan = invokeTimespan;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
