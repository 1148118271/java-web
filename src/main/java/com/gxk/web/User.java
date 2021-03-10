package com.gxk.web;



import com.gxk.Request;
import com.gxk.Response;

import java.util.HashMap;
import java.util.Map;

public class User {

    public void getPage(Request request, Response response) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", "首页");
        map.put("params", request.getParameters());
        response.writeHtml("view/index.html", map);
    }

    public void queryInfo(Request request, Response response) throws Exception {
        Map<String, Object> parameters = request.getParameters();
        parameters.put("success", true);
        parameters.put("msg", "查询成功!");
        response.writeJson(parameters);
    }
}
