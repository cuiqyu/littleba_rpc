package com.limpid.annotation;

import com.limpid.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * RPC扫描注解
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 16:07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(CustomScannerRegistrar.class)
public @interface RpcScan {

    String[] basePackage();

}
