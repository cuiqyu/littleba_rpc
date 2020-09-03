package com.limpid.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * RPC消息类型
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 11:21
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcMessageTypeEnum {

    HEARTBEAT("心跳");

    private final String desc;

}
