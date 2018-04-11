package com.luoromeo.rpc.services.impl;

import com.luoromeo.rpc.services.AddCalculate;

public class AddCalculateImpl implements AddCalculate {
    // 两数相加
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
