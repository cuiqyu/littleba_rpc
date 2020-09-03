package com.limpid.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例工厂
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 13:16
 */
public class SingletonFactory {

    /**
     * 缓存对象
     */
    private static final Map<String, Object> OBJECTMAP = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> clazz) {
        // 先从缓存中获取，缓存中没有则新建并存入缓存
        String key = clazz.toString();
        Object instance = OBJECTMAP.get(key);
        if (null == instance) {
            // double check
            synchronized (clazz) {
                // 创建实例并存入缓存，如果有则直接返回原来的
                try {
                    instance = OBJECTMAP.putIfAbsent(key, clazz.getDeclaredConstructor().newInstance());
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return clazz.cast(instance);
    }

}
