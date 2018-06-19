package com.happok.live.activitycontrol.ffmpeg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Object load(String path, Class<?> cl) {
        InputStream is = null;
        try {
            is = getInputStream(path);
        } catch (FileNotFoundException e) {
            //尝试从web目录读取
            String newpath = CommonUtil.getProjectRootPath() + path;
            logger.error("尝试从web目录读取配置文件：" + newpath);
            try {
                is = getInputStream(newpath);
                logger.error("web目录读取到配置文件：" + newpath);
            } catch (FileNotFoundException e1) {
                logger.error("没找到配置文件，读取默认配置文件");
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                try {
                    is = classloader.getResourceAsStream("cc/eguid/FFmpegCommandManager/config/defaultFFmpegConfig.properties");
                    logger.info("读取默认配置文件：defaultFFmpegConfig.properties");
                } catch (Exception e2) {
                    logger.error("没找到默认配置文件:defaultFFmpegConfig.properties");
                    return null;
                }
            }
        }
        if (is != null) {
            Properties pro = new Properties();
            try {
                logger.info("加载配置文件...");
                pro.load(is);
                logger.info("加载配置文件完毕");
                return load(pro, cl);
            } catch (IOException e) {
                logger.info("加载配置文件失败");
                return null;
            }

        }
        return null;
    }

    public static Object load(Properties pro, Class<?> cl) {
        try {
            Map<String, Object> map = getMap(pro);
            System.err.println("读取的配置项：" + map);
            Object obj = ReflectUtil.mapToObj(map, cl);
            System.err.println("转换后的对象：" + obj);
            return obj;
        } catch (InstantiationException e) {
            System.err.println("加载配置文件失败");
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("加载配置文件失败");
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("加载配置文件失败");
            return null;
        } catch (InvocationTargetException e) {
            System.err.println("加载配置文件失败");
            return null;
        }
    }

    public static InputStream getInputStream(String path) throws FileNotFoundException {
        return new FileInputStream(path);
    }

    public static Map<String, Object> getMap(String path) {
        Properties pro = new Properties();
        try {
            pro.load(getInputStream(path));
            return getMap(pro);
        } catch (IOException e) {
            return null;
        }
    }

    public static Map<String, Object> getMap(String path, boolean isRootPath) {
        return getMap(isRootPath ? CommonUtil.getProjectRootPath() + path : path);
    }

    public static Map<String, Object> getMap(Properties pro) {
        if (pro == null || pro.isEmpty() || pro.size() < 1) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (Entry<Object, Object> en : pro.entrySet()) {
            String key = (String) en.getKey();
            Object value = en.getValue();
            map.put(key, value);
        }
        return map;
    }
}
