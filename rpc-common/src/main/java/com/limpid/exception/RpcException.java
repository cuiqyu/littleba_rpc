package com.limpid.exception;

import com.limpid.enumeration.RpcErrorMessageEnum;

/**
 * RPC异常
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 10:48
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum rpcErrorMessage, String detail) {
        super(rpcErrorMessage.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessage) {
        super(rpcErrorMessage.getMessage());
    }

}
