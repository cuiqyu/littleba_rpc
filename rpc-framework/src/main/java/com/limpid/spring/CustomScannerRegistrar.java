package com.limpid.spring;

import com.limpid.annotation.RpcScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * 自定义扫描注册器
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 16:27
 */
@Slf4j
public class CustomScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    /**
     * 自定义扫描注解的扫描包的属性名，需要跟@RpcScan的属性一致
     *
     * @See com.limpid.annotation.RpcScan
     */
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    /**
     * 需要扫描的spring相关的包
     */
    private static final String SPRING_BEAN_BASE_PACKAGE = "com.limpid.spring";

    private ResourceLoader resourceLoader;


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        // 获取 RpcScan注解的属性与值的内容
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        // 获取注解的扫描包字段
        String[] rpcScanBasePackages = new String[0];
        if (null != annotationAttributes) {
            // 获取注解中配置的指定扫描包对应的值
            rpcScanBasePackages = annotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        // 判断scanBasePackage是否为空，如果为空则表明没有指定扫描的包，则默认扫描当前注解所在的包
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }
        // 扫描@RpcSace注解
        CustomScanner rpcServiceScanner = new CustomScanner(registry, RpcScan.class);
        // 扫描@Component注解
        CustomScanner springBeanScanner = new CustomScanner(registry, Component.class);
        if (null != resourceLoader) {
            rpcServiceScanner.setResourceLoader(resourceLoader);
            springBeanScanner.setResourceLoader(resourceLoader);
        }

        int springBeanAmount = springBeanScanner.scan(SPRING_BEAN_BASE_PACKAGE);
        log.info("springBeanScanner扫描的数量 [{}]", springBeanAmount);
        int scanCount = rpcServiceScanner.scan(rpcScanBasePackages);
        log.info("rpcServiceScanner扫描的数量 [{}]", scanCount);
    }

}
