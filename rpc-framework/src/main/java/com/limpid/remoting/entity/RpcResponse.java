package com.limpid.remoting.entity;

import com.limpid.enumeration.RpcResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * RPC响应结果
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 11:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 6004709992898356733L;

    /**
     * 唯一请求id
     */
    private String requestId;
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应描述
     */
    private String message;
    /**
     * 响应内容
     */
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        response.setMessage(RpcResponseCode.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCode.getCode());
        response.setMessage(rpcResponseCode.getMessage());
        return response;
    }

}
