package com.limpid.provider;

import com.limpid.entity.RpcServiceProperties;
import com.limpid.enumeration.RpcErrorMessageEnum;
import com.limpid.exception.RpcException;
import com.limpid.extensions.SpiExtensionsLoader;
import com.limpid.registry.ServiceRegistry;
import com.limpid.remoting.transport.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 17:36
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    /**
     * 服务注册缓存
     * key: rpc服务名称(interface name + version + group)
     * value: 服务实例对象
     */
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 注册的服务集合
     */
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();
    /**
     * 服务注册器
     */
    private final ServiceRegistry serviceRegistry = SpiExtensionsLoader.getSpiExtensionsLoader(ServiceRegistry.class).getExtension("zk");

    @Override
    public void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        // 获取rpc服务名称
        String serviceName = rpcServiceProperties.getServiceName();
        // 判断是否已经注册过
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        log.info("添加服务: {} 接口:{}", serviceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        Object service = serviceMap.get(rpcServiceProperties.toRpcServiceName());
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            Class<?> serviceRelatedInterface = service.getClass().getInterfaces()[0];
            String serviceName = serviceRelatedInterface.getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            this.addService(service, serviceRelatedInterface, rpcServiceProperties);
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(), new InetSocketAddress(host, NettyServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }

    @Override
    public void publishService(Object service) {
        this.publishService(service, RpcServiceProperties.builder().group("").version("").build());
    }

}
