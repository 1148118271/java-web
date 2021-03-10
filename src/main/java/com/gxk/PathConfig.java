package com.gxk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;

/**
 * web.properties 解析类
 * @author gaoXiangKang
 * @date 2021-03-10
 */
public class PathConfig {
    private static final Map<String, String> GET_MAP = new HashMap<>();
    private static final Map<String, String> POST_MAP = new HashMap<>();

    private static String PACKAGE_PATH;


    public static String getPackagePath() {
        return PACKAGE_PATH;
    }

    public static Map<String, String> getGetMap() {
        return GET_MAP;
    }

    public static String getGetMapValue(String k) {
        return GET_MAP.get(k);
    }

    public static Map<String, String> getPostMap() {
        return POST_MAP;
    }

    public static String getPostMapValue(String k) {
        return POST_MAP.get(k);
    }

    // 初始化加载配置文件
    // get 和 post 请求分开存储到缓存
    public static void initUri() throws Exception {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream resourceAsStream = PathConfig.class.getResourceAsStream("/web.properties");
        // 使用properties对象加载输入流
        properties.load(resourceAsStream);
        // 获取key对应的value值
        Set<String> ks = properties.stringPropertyNames();
        for (String k : ks) {
            if ("package.path".equals(k)) {
                PACKAGE_PATH = properties.getProperty(k);
            }
            String[] url = k.split("\\.");
            if (url[0].equals("get")) {
                String u = add(url);
                GET_MAP.put(u, properties.getProperty(k));
            }
            else if (url[0].equals("post")) {
                String u = add(url);
                POST_MAP.put(u, properties.getProperty(k));
            }
        }
        resourceAsStream.close();
    }

    private static String add(String[] url) {
        StringJoiner sj = new StringJoiner("/", "/", "");
        for (int i = 0; i < url.length; i++) {
            if (i > 0) {
                sj.add(url[i]);
            }
        }
        return sj.toString();
    }

}
