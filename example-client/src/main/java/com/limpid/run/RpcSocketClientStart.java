package com.limpid.run;

import com.limpid.entity.RpcServiceProperties;
import com.limpid.proxy.RpcClientProxy;
import com.limpid.remoting.transport.ClientTransport;
import com.limpid.remoting.transport.socket.SocketRpcClient;
import com.limpid.service.HelloWorldService;

/**
 * socket消费服务
 *
 * @auther cuiqiongyu
 * @create 2020/9/3 14:32
 */
public class RpcSocketClientStart {

    public static void main(String[] args) {
        ClientTransport clientTransport = new SocketRpcClient();
        HelloWorldService cppService = new RpcClientProxy(clientTransport, RpcServiceProperties.builder()
                .group("hello").version("1.0").build()).getProxy(HelloWorldService.class);
        String hello = cppService.hello();
        System.out.println(hello);
        HelloWorldService javaService = new RpcClientProxy(clientTransport, RpcServiceProperties.builder()
                .group("hello").version("2.0").build()).getProxy(HelloWorldService.class);
        String hello1 = javaService.hello();
        System.out.println(hello1);
        HelloWorldService pythonService = new RpcClientProxy(clientTransport, RpcServiceProperties.builder()
                .group("hello").version("3.0").build()).getProxy(HelloWorldService.class);
        String hello2 = pythonService.hello();
        System.out.println(hello2);
    }

}
