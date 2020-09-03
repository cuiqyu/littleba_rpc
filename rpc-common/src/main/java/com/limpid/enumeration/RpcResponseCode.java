package com.limpid.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author shuang.kou
 * @createTime 2020年05月12日 16:24:00
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {

    SUCCESS(200, "远程调用成功"),
    FAIL(500, "远程调用失败");

    private final int code;

    private final String message;

}
