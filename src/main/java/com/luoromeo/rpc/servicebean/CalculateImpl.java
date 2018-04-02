package com.luoromeo.rpc.servicebean;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年03月29日 16:14
 * @modified By
 */
public class CalculateImpl implements Calculate {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
