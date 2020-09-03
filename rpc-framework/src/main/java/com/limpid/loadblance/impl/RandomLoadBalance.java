package com.limpid.loadblance.impl;

        import java.util.List;
        import java.util.Random;

/**
 * 负载均衡随机算法
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 11:23
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelectAddress(List<String> serviceAddresses) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }

}
