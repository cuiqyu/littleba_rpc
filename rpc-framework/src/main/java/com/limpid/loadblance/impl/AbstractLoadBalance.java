package com.limpid.loadblance.impl;

import com.limpid.loadblance.LoadBalance;
import com.limpid.utils.exception.ExceptionAssertUtil;

import java.util.List;

/**
 * 负载均衡抽象实现
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 10:44
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    public String selectServiceAddress(List<String> serviceAddresses) {
        ExceptionAssertUtil.notEmpty(serviceAddresses, "服务器地址列表不能为空");
        return (serviceAddresses.size() == 1) ? serviceAddresses.get(0) : doSelectAddress(serviceAddresses);
    }

    protected abstract String doSelectAddress(List<String> serviceAddresses);

}
