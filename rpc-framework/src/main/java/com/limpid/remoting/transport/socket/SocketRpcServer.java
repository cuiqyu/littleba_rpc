package com.limpid.remoting.transport.socket;

import com.limpid.config.CustomShutdownHook;
import com.limpid.entity.RpcServiceProperties;
import com.limpid.factory.SingletonFactory;
import com.limpid.provider.ServiceProvider;
import com.limpid.provider.ServiceProviderImpl;
import com.limpid.remoting.entity.RpcRequest;
import com.limpid.remoting.entity.RpcResponse;
import com.limpid.remoting.handler.RpcRequestHandler;
import com.limpid.utils.threadpool.ThreadPoolFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static com.limpid.remoting.transport.netty.server.NettyServer.PORT;

/**
 * @auther cuiqiongyu
 * @create 2020/9/3 11:25
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public void registerService(Object serviceName) {
        serviceProvider.publishService(serviceName);
    }

    public void registerService(Object serviceName, RpcServiceProperties properties) {
        serviceProvider.publishService(serviceName, properties);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }

    class SocketRpcRequestHandlerRunnable implements Runnable {
        private final Socket socket;
        private final RpcRequestHandler rpcRequestHandler;

        public SocketRpcRequestHandlerRunnable(Socket socket) {
            this.socket = socket;
            this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
        }

        @Override
        public void run() {
            log.info("服务器通过线程处理来自客户端的消息[{}]", Thread.currentThread().getName());
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                Object result = rpcRequestHandler.handle(rpcRequest);
                objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
                objectOutputStream.flush();
            } catch (IOException | ClassNotFoundException e) {
                log.error("occur exception:", e);
            }
        }

    }

}
