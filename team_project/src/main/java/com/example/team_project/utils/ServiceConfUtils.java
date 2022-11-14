package com.example.team_project.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 加载service.properties配置文件的工具类
 */
public class ServiceConfUtils {
    private static final Properties pro;
    /**
     * 从listener获取到的配置信息
     */
    public static final String CONTEXT_PATH = "contextPath";

    static {
        pro = new Properties();
        try {
            pro.load(ServiceConfUtils.class.getClassLoader().getResourceAsStream("conf/service.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConfig(String configName) {
        return pro.getProperty(configName);
    }

    public static boolean addConfig(String key, String value) {
        String config = pro.getProperty(key);
        if (config != null) {
            return false;
        }
        pro.put(key,value);
        return true;
    }

    public static boolean containsConfig(String configName){
        return pro.containsKey(configName);
    }



}
