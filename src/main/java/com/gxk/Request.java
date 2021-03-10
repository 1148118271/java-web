package com.gxk;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http请求组装类
 * @author gaoXiangKang
 * @date 2021-03-10
 */
public class Request {

    private FullHttpRequest request;

    private Map<String, Object> params;

    public Request(FullHttpRequest httpRequest) {
        this.request = httpRequest;

        if (request.method() == HttpMethod.GET) {
            this.setGetParams();
        }
        else if (request.method() == HttpMethod.POST) {
            this.setPostParams();
        }

    }

    public Request() {}

    public String getUri() {
        return request.uri();
    }

    public String getMethod(){
        return request.method().name();
    }

    public Map<String, Object> getParameters() {
       return params;
    }

    public Object getParameter(String name) {
        if (params == null || params.size() <= 0) {
            return null;
        }
        return params.get(name);
    }

    private void setGetParams() {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Map<String, Object> map = new HashMap<>();
        parameters.forEach((k, v) -> {
            if (v != null && v.size() > 0) {
                map.put(k, v.get(0));
            }
        });

        this.params = map;
    }

    private void setPostParams() {
        ByteBuf content = request.content();
        String msg = content.toString(CharsetUtil.UTF_8);
        Gson gson = new Gson();
        this.params = gson.fromJson(msg, Map.class);
    }
}
