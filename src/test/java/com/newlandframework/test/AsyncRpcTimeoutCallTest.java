/**
 * Copyright (C) 2017 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.luoromeo.rpc.async.AsyncCallObject;
import com.luoromeo.rpc.async.AsyncCallback;
import com.luoromeo.rpc.async.AsyncInvoker;
import com.luoromeo.rpc.exception.InvokeTimeoutException;
import com.luoromeo.rpc.services.CostTimeCalculate;
import com.luoromeo.rpc.services.pojo.CostTime;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:AsyncRpcTimeoutCallTest.java
 * @description:AsyncRpcTimeoutCallTest功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2017/10/28
 */
public class AsyncRpcTimeoutCallTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        final CostTimeCalculate calculate = (CostTimeCalculate) context.getBean("costTime");

        AsyncInvoker invoker = new AsyncInvoker();

        try {
            CostTime elapse0 = invoker.submit(calculate::busy);

            System.out.println("1 async nettyrpc call:[" + "result:" + elapse0 + ", status:[" + ((AsyncCallObject) elapse0)._getStatus() + "]");
        } catch (InvokeTimeoutException e) {
            System.out.println(e.getMessage());
            context.destroy();
            return;
        }

        context.destroy();
    }
}
