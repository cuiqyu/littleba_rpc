package com.limpid;

import com.limpid.extensions.SpiExtensionsLoader;
import com.limpid.registry.ServiceDiscovery;
import com.limpid.registry.ServiceRegistry;
import com.limpid.serializer.Serializer;

/**
 * @auther cuiqiongyu
 * @create 2020/9/2 10:36
 */
public class Test {

    public static void main(String[] args) {
        Serializer serializer = SpiExtensionsLoader.getSpiExtensionsLoader(Serializer.class).getExtension("kryo");
        ServiceDiscovery serviceDiscovery = SpiExtensionsLoader.getSpiExtensionsLoader(ServiceDiscovery.class).getExtension("zk");
        ServiceRegistry serviceRegistry = SpiExtensionsLoader.getSpiExtensionsLoader(ServiceRegistry.class).getExtension("zk");
        serializer = SpiExtensionsLoader.getSpiExtensionsLoader(Serializer.class).getExtension("kryo");
        serviceDiscovery = SpiExtensionsLoader.getSpiExtensionsLoader(ServiceDiscovery.class).getExtension("zk");
        serviceRegistry = SpiExtensionsLoader.getSpiExtensionsLoader(ServiceRegistry.class).getExtension("zk");
        System.out.println(serializer.toString());
        System.out.println(serviceDiscovery.toString());
        System.out.println(serviceRegistry.toString());
        System.out.println("===================");
    }

}
