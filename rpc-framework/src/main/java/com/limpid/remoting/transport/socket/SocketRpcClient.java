package com.limpid.remoting.transport.socket;

import com.limpid.entity.RpcServiceProperties;
import com.limpid.exception.RpcException;
import com.limpid.extensions.SpiExtensionsLoader;
import com.limpid.registry.ServiceDiscovery;
import com.limpid.remoting.entity.RpcRequest;
import com.limpid.remoting.transport.ClientTransport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于 Socket 传输 RpcRequest
 *
 * @auther cuiqiongyu
 * @create 2020/9/3 11:09
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements ClientTransport {

    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = SpiExtensionsLoader.getSpiExtensionsLoader(ServiceDiscovery.class).getExtension("zk");
    }

    /**
     * 发送RPC请求
     *
     * @param rpcRequest 请求内容
     * @return
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 根据RPC请求获取服务名称
        String rpcServiceName = RpcServiceProperties.builder().serviceName(rpcRequest.getInterfaceName())
                .group(rpcRequest.getGroup()).version(rpcRequest.getVersion()).build().toRpcServiceName();
        // 根据服务名称获取对应的服务器地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 通过输出流将数据发送给服务器
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 从输入流读取RpcResponse
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
    }

}
