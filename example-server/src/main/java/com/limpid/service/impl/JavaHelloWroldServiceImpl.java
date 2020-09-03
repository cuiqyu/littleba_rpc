package com.limpid.service.impl;

import com.limpid.annotation.RpcService;
import com.limpid.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther cuiqiongyu
 * @create 2020/9/3 13:24
 */
@RpcService(version = "2.0", group = "hello")
@Slf4j
public class JavaHelloWroldServiceImpl implements HelloWorldService {

    @Override
    public String hello() {
        String result = "hello java, welcome to the RPC framework";
        return result;
    }

}
