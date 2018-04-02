package com.luoromeo.rpc.serialize.support.hessian;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @description 序列化/反序列化对象工厂池
 * @author zhanghua.luo
 * @date 2018年03月30日 16:20
 * @modified By
 */
public class HessianSerializeFactory extends BasePooledObjectFactory<HessianSerialize> {
    @Override
    public HessianSerialize create() throws Exception {
        return createHessioan();
    }

    @Override
    public PooledObject<HessianSerialize> wrap(HessianSerialize hessianSerialize) {
        return new DefaultPooledObject<>(hessianSerialize);
    }

    private HessianSerialize createHessioan() {
        return new HessianSerialize();
    }
}
