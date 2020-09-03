package com.limpid.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * rpc服务注解
 *
 * @auther cuiqiongyu
 * @create 2020/9/3 10:05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Component
public @interface RpcService {

    /**
     * 服务版本号，默认为空
     *
     * @return
     */
    String version() default "";

    /**
     * 服务分组，默认为空
     *
     * @return
     */
    String group() default "";

}
