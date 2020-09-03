package com.limpid.entity;

import lombok.*;

import java.io.Serializable;

/**
 * RPC服务信息
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 11:56
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceProperties implements Serializable {

    private static final long serialVersionUID = -1197476332869945909L;

    /**
     * 版本号
     */
    private String version;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务分组，当接口有多个实现的时候，可以使用分组
     */
    private String group;

    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

}
