package com.limpid.service.impl;

import com.limpid.annotation.RpcService;
import com.limpid.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther cuiqiongyu
 * @create 2020/9/3 13:26
 */
@Slf4j
@RpcService(version = "1.0", group = "hello")
public class CppHelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String hello() {
        String result = "hello c++, welcome to the RPC framework";
        return result;
    }

}
