package com.luoromeo.rpc.services.impl;

import com.luoromeo.rpc.services.MultiCalculate;

public class MultiCalculateImpl implements MultiCalculate {
    // 两数相乘
    @Override
    public int multi(int a, int b) {
        return a * b;
    }
}
