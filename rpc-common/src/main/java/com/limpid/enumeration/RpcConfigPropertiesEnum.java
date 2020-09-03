package com.limpid.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC配置参数枚举
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 14:23
 */
@AllArgsConstructor
@Getter
public enum RpcConfigPropertiesEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;

}
