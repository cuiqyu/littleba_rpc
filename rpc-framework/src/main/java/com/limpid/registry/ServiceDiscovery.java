package com.limpid.registry;

import com.limpid.extensions.SPI;

import java.net.InetSocketAddress;

/**
 * 服务发现
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 11:57
 */
@SPI
public interface ServiceDiscovery {

    /**
     * 根据rpc服务名称查询服务地址
     *
     * @param rpcServiceName rpc服务名称
     * @return 服务器地址
     */
    InetSocketAddress lookupService(String rpcServiceName);

}
