package com.limpid.run;

import com.esotericsoftware.minlog.Log;
import com.limpid.annotation.RpcScan;
import com.limpid.entity.RpcServiceProperties;
import com.limpid.factory.SingletonFactory;
import com.limpid.remoting.transport.socket.SocketRpcServer;
import com.limpid.service.HelloWorldService;
import com.limpid.service.impl.PythonHelloWorldServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * socket启动服务
 *
 * @auther cuiqiongyu
 * @create 2020/9/3 13:28
 */
@RpcScan(basePackage = "com.limpid.service")
@Slf4j
public class RpcSocketServerStart {

    public static void main(String[] args) {
        // 方式一：启动注解扫描RpcService相关的服务类自动注册
        /**
         * com.limpid.service.impl.CppHelloWorldServiceImpl
         * com.limpid.service.impl.JavaHelloWroldServiceImpl
         */
        new AnnotationConfigApplicationContext(RpcSocketServerStart.class);
        // 方式二：手动将服务注册至RPC中
        HelloWorldService pythonHelloWorldService = new PythonHelloWorldServiceImpl();
        SocketRpcServer socketRpcServer = SingletonFactory.getInstance(SocketRpcServer.class);
        socketRpcServer.registerService(pythonHelloWorldService, RpcServiceProperties.builder().version("3.0").group("hello").build());
        socketRpcServer.start();
        log.info("服务端启动成功=====================");
    }

}
