
package com.luoromeo.rpc.services;

import com.luoromeo.rpc.services.pojo.CostTime;


public interface CostTimeCalculate {
    CostTime calculate();

    CostTime busy();
}
