package com.limpid.registry.zk.util;

import com.limpid.enumeration.RpcConfigPropertiesEnum;
import com.limpid.exception.RpcException;
import com.limpid.utils.file.PropertiesFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zookeeper客户端工具curator
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 13:36
 */
@Slf4j
public class CuratorUtils {

    /**
     * 基础睡眠时间
     */
    private static final int BASE_SLEEP_TIME = 1000;
    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 3;
    /**
     * zookeeper注册根路径
     */
    public static final String ZK_REGISTER_ROOT_PATH = "/littleba-rpc";
    /**
     * 服务对应的地址映射
     */
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    /**
     * 注册路径集合
     */
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    /**
     * zookeeper客户端
     */
    private static CuratorFramework zkClient;
    /**
     * 默认zookeeper连接地址
     */
    private static String default_zookeeper_address = "127.0.0.1:2181";

    private CuratorUtils() {
    }

    /**
     * 获取zookeeper客户端
     *
     * @return zookeeper客户端
     */
    public static CuratorFramework getZkClient() {
        // 如果zkClient客户端不为空且已启动，则直接返回
        if (null != zkClient && CuratorFrameworkState.STARTED == zkClient.getState()) {
            return zkClient;
        }

        // 检查用户是否设置了zk的连接地址
        Properties properties = PropertiesFileUtils.readPropertiesFile(RpcConfigPropertiesEnum.RPC_CONFIG_PATH.getPropertyValue());
        if (null != properties) {
            default_zookeeper_address = properties.getProperty(RpcConfigPropertiesEnum.ZK_ADDRESS.getPropertyValue());
        }

        // 重试策略。重试3次，将增加重试之间的睡眠时间。
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                // 要连接的服务器（可以是服务器列表）
                .connectString(default_zookeeper_address)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }

    /**
     * 创建zookeeper的持久节点【与临时节点不同，客户端断开连接时不会删除持久节点】
     *
     * @param zkClient zookeer客户端
     * @param path     节点路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("该节点已经存在. 这个节点是:[{}]", path);
            } else {
                // 例如: /littleba-rpc/com.limpid.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("节点创建成功，这个节点是:[{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 清空注册表数据
     */
    public static void clearRegistry(CuratorFramework zkClient) {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                zkClient.delete().forPath(p);
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e.getCause());
            }
        });
        log.info("服务器上所有注册的服务都将被清除:[{}]", REGISTERED_PATH_SET.toString());
    }

    /**
     * 根据服务名称获取子节点列表
     *
     * @param zkClient       zookeeper客户端
     * @param rpcServiceName 接口服务名称
     * @return
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        // 先从缓存中获取
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }

        List<String> result;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            // 存储缓存
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            // 注册监听器，监听对指定节点的更改
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }

        return result;
    }

    /**
     * 注册监听器监听指定节点的更改
     *
     * @param rpcServiceName 接口服务名称
     * @param zkClient       zookeeper客户端
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }


}
