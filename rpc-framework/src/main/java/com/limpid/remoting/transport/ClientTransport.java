package com.limpid.remoting.transport;

import com.limpid.remoting.entity.RpcRequest;

/**
 * 客户端发送数据服务
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 15:22
 */
public interface ClientTransport {

    /**
     * 给服务器发送rpc请求并接受服务器的返回内容
     *
     * @param rpcRequest 请求内容
     * @return 服务器的响应内容
     */
    Object sendRpcRequest(RpcRequest rpcRequest);

}
