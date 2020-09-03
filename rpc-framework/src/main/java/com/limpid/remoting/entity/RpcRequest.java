package com.limpid.remoting.entity;

import com.limpid.entity.RpcServiceProperties;
import com.limpid.enumeration.RpcMessageTypeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * RPC请求内容
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 11:52
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 5211542951283648845L;

    /**
     * 请求唯一id
     */
    private String requestId;
    /**
     * 请求的接口名称
     */
    private String interfaceName;
    /**
     * 请求的方法名称
     */
    private String methodName;
    /**
     * 参数列表
     */
    private Object[] parameters;
    /**
     * 参数类型列表
     */
    private Class<?>[] paramTypes;
    /**
     * 消息类型
     */
    private RpcMessageTypeEnum rpcMessageTypeEnum;
    /**
     * 服务版本号
     */
    private String version;
    /**
     * 服务组
     */
    private String group;

    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder().serviceName(this.getInterfaceName())
                .version(this.getVersion())
                .group(this.getGroup()).build();
    }

}
