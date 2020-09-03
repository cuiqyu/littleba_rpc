package com.limpid.extensions;

import com.limpid.utils.Holder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spi扩展加载
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 17:48
 */
@Slf4j
public class SpiExtensionsLoader<T> {

    /**
     * 指定服务扩展的加载目录
     */
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";
    /**
     * 服务扩展加载器缓存Map
     */
    private static final Map<Class<?>, SpiExtensionsLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    /**
     * 指定类对应的实例缓存Map
     */
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class<?> type;
    /**
     * 指定名称对应的类缓存Map
     */
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private SpiExtensionsLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 根据指定的class类型获取服务扩展加载器
     *
     * @param type
     * @param <S>
     * @return
     */
    public static <S> SpiExtensionsLoader<S> getSpiExtensionsLoader(Class<S> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @SPI");
        }

        // 先从缓存中获取指定的服务扩展加载器，如果没有则创建并存入
        SpiExtensionsLoader<S> spiExtensionsLoader = (SpiExtensionsLoader<S>) EXTENSION_LOADERS.get(type);
        if (null == spiExtensionsLoader) {
            EXTENSION_LOADERS.putIfAbsent(type, new SpiExtensionsLoader(type));
            spiExtensionsLoader = (SpiExtensionsLoader<S>) EXTENSION_LOADERS.get(type);
        }
        return spiExtensionsLoader;
    }

    /**
     * 根据指定名称获取实现类
     *
     * @param name
     * @return
     */
    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name should not be null or empty.");
        }

        // 先从实例缓存中获取，如果获取不到则创建并放入
        Holder<Object> holder = cachedInstances.get(name);
        if (null == holder) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }

        // 从holder中获取实例
        Object instance = holder.getValue();
        if (null == instance) {
            // 创建并放入
            // double check
            synchronized (holder) {
                if (null == instance) {
                    // 根据名称创建扩展服务
                    instance = createExtension(name);
                    holder.setValue(instance);
                }
            }
        }

        return (T) instance;
    }

    /**
     * 根据指定名称加载扩展服务
     *
     * @param name
     * @return
     */
    private T createExtension(String name) {
        // 从文件中加载类型为T的所有扩展类，并按名称获取特定的扩展类
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("No such extension of name " + name);
        }

        // 先从缓存中获取，缓存没有则创建并放入
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (null == instance) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }


    private Map<String, Class<?>> getExtensionClasses() {
        // 从缓存中获取加载的扩展类
        Map<String, Class<?>> classes = cachedClasses.getValue();
        // double check
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.getValue();
                if (classes == null) {
                    classes = new HashMap<>();
                    // 从扩展目录加载所有扩展
                    loadDirectory(classes);
                    cachedClasses.setValue(classes);
                }
            }
        }
        return classes;
    }

    /**
     * 从扩展目录中加载配置
     *
     * @param extensionClasses
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = SpiExtensionsLoader.SERVICE_DIRECTORY + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = SpiExtensionsLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 加载指定的资源
     *
     * @param extensionClasses
     * @param classLoader
     * @param resourceUrl
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), "UTF-8"))) {
            String line;
            // 一行一行读，读取所有行
            while ((line = reader.readLine()) != null) {
                // 获取注释索引
                final int ci = line.indexOf('#');
                if (ci >= 0) {
                    // #后的字符串是注释，因此我们忽略它
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        final int ei = line.indexOf('=');
                        String name = line.substring(0, ei).trim();
                        String clazzName = line.substring(ei + 1).trim();
                        // 我们的SPI使用键值对，因此它们都不能为空
                        if (name.length() > 0 && clazzName.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(name, clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage());
                    }
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
