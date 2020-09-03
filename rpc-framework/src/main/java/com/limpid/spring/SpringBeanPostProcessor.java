package com.limpid.spring;

import com.limpid.annotation.RpcService;
import com.limpid.entity.RpcServiceProperties;
import com.limpid.factory.SingletonFactory;
import com.limpid.provider.ServiceProvider;
import com.limpid.provider.ServiceProviderImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @auther cuiqiongyu
 * @create 2020/9/3 10:24
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;

    public SpringBeanPostProcessor() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    @SneakyThrows
    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // 获取RpcService注解
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            // 构建RPC服务参数
            RpcServiceProperties build = RpcServiceProperties.builder().group(rpcService.group()).version(rpcService.version()).build();
            // 发布RPC服务
            serviceProvider.publishService(bean, build);
        }
        return bean;
    }

}
