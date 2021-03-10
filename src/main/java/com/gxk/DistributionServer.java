package com.gxk;

import io.netty.util.internal.StringUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 请求分发器
 * @author gaoXiangKang
 * @date 2021-03-10
 */
public class DistributionServer {

    public void process(Request request, Response response) throws Exception {
        String uri = urlProcess(request.getUri());
        // 处理get请求
        if (request.getMethod().equalsIgnoreCase("GET")) {
            String getMapValue = PathConfig.getGetMapValue(uri);
            if (StringUtil.isNullOrEmpty(getMapValue)) {
                response.r404();
                return;
            }
            runMethod(getMapValue, request, response);
        }
        // 处理post请求
        else if (request.getMethod().equalsIgnoreCase("POST")) {
            String postMapValue = PathConfig.getPostMapValue(uri);
            if (StringUtil.isNullOrEmpty(postMapValue)) {
                response.r404();
                return;
            }
            runMethod(postMapValue, request, response);
        }
    }

    private void runMethod(String v, Request request, Response response) throws Exception {
        Map<String, Object> map = getClassAndMethod(v);
        Class<?> clazz = (Class<?>) map.get("clazz");
        String methodName = (String) map.get("method");
        Method method = clazz.getMethod(methodName, Request.class, Response.class);
        method.invoke(clazz.newInstance(), request, response);
    }

    private Map<String, Object> getClassAndMethod(String v) throws Exception {
        Map<String, Object> map = new HashMap<>(2);
        String className = "";
        if (v.contains("_")) {
            String[] s = v.split("_");
            className = s[0];
            map.put("method", s[1]);
        } else {
            className = v;
        }
        String clazz = PathConfig.getPackagePath() + "." + className;
        map.put("clazz", Class.forName(clazz));
        return map;
    }

    private String urlProcess(String u) {
        String ru = "";
        if (u.contains("?")) {
           ru = u.split("\\?")[0];
        }
        else {
            ru = u;
        }
        if (ru.lastIndexOf("/") == (ru.length() - 1)) {
            ru = ru.substring(0, (ru.length() - 1));
        }
        return ru;
    }
}
