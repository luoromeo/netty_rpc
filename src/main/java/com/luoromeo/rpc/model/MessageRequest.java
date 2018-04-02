package com.luoromeo.rpc.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @description 服务请求结构
 * @author zhanghua.luo
 * @date 2018年03月28日 23:20
 * @modified By
 */
@Getter
@Setter
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String className;

    private String methodName;

    private Class<?>[] typeParameters;

    private Object[] parametersVal;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "typeParameters", "parametersVal");
    }
}
