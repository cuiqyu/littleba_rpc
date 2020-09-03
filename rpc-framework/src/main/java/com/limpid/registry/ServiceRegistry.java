package com.limpid.registry;

import com.limpid.extensions.SPI;

import java.net.InetSocketAddress;

/**
 * 服务注册
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 11:57
 */
@SPI
public interface ServiceRegistry {

    /**
     * 注册RPC服务
     *
     * @param rpcServiceName    rpc服务名称
     * @param inetSocketAddress 服务器地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
