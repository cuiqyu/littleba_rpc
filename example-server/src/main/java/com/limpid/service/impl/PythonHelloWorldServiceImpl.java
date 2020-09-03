package com.limpid.service.impl;

import com.limpid.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther cuiqiongyu
 * @create 2020/9/3 13:27
 */
@Slf4j
public class PythonHelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String hello() {
        String result = "hello pyhone, welcome to the RPC framework";
        return result;
    }

}
