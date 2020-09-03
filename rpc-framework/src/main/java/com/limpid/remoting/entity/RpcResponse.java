package com.limpid.remoting.entity;

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

}
