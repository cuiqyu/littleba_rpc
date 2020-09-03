package com.limpid.remoting.transport.netty.server;

import com.limpid.entity.RpcServiceProperties;
import com.limpid.extensions.SpiExtensionsLoader;
import com.limpid.factory.SingletonFactory;
import com.limpid.provider.ServiceProvider;
import com.limpid.provider.ServiceProviderImpl;
import com.limpid.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @auther cuiqiongyu
 * @create 2020/9/2 18:11
 */
@Slf4j
@Component
public class NettyServer {

    private final Serializer kryoSerializer = SpiExtensionsLoader.getSpiExtensionsLoader(Serializer.class).getExtension("kyro");
    public static final int PORT = 9998;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);

    public void registerService(Object serviceName) {
        serviceProvider.publishService(serviceName);
    }

    public void registerService(Object serviceName, RpcServiceProperties properties) {
        serviceProvider.publishService(serviceName, properties);
    }

}
