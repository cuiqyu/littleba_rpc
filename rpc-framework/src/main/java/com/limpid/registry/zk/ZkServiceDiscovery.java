package com.limpid.registry.zk;

import com.limpid.enumeration.RpcErrorMessageEnum;
import com.limpid.exception.RpcException;
import com.limpid.loadblance.LoadBalance;
import com.limpid.loadblance.impl.RandomLoadBalance;
import com.limpid.registry.ServiceDiscovery;
import com.limpid.registry.zk.util.CuratorUtils;
import com.limpid.utils.ExceptionAssertUtil;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * zookeeper服务发现
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 13:10
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        this.loadBalance = new RandomLoadBalance();
    }

    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        ExceptionAssertUtil.notEmpty(rpcServiceName, "rpc服务名称不能为空");
        // 根据接口服务获取对应的zk上注册的节点名称
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceAddresses = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtils.isEmpty(serviceAddresses)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }

        // 使用负载均衡选取一个服务器地址
        String serviceAddress = loadBalance.selectServiceAddress(serviceAddresses);
        log.info("成功找到服务地址:[{}]", serviceAddress);
        // 按照:拆分
        String[] split = serviceAddress.split(":");
        String host = split[0];
        int port = Integer.parseInt(split[1]);
        return new InetSocketAddress(host, port);
    }

}
