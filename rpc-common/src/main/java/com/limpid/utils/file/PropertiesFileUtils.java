package com.limpid.utils.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * properties文件工具
 *
 * @auther cuiqiongyu
 * @create 2020/9/2 14:34
 */
@Slf4j
public class PropertiesFileUtils {

    private PropertiesFileUtils() {
    }

    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String rpcConfigPath = "";
        if (url != null) {
            rpcConfigPath = url.getPath() + fileName;
        }
        Properties properties = null;
        try (FileInputStream fileInputStream = new FileInputStream(rpcConfigPath)) {
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            log.error("读取属性文件时发生异常 [{}]", fileName);
        }
        return properties;
    }


}
