package com.luoromeo.rpc.boot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年03月29日 16:13
 * @modified By
 */
public class RpcServerStarter {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:rpc-invoke-config.xml");
    }
}

