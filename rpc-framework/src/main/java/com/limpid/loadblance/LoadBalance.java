package com.limpid.loadblance;

import java.util.List;

/**
 * 负载均衡接口
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 10:31
 */
public interface LoadBalance {

    /**
     * 从给出的一个服务器地址列表中根据相关算法选出一个服务器
     *
     * @param serviceAddresses
     * @return
     */
    public String selectServiceAddress(List<String> serviceAddresses);

}
