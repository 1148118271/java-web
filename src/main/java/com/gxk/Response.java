package com.gxk;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.util.Map;

/**
 * http返回组装类
 * @author gaoXiangKang
 * @date 2021-03-10
 */
public class Response {

    private ChannelHandlerContext channelHandlerContext;


    public Response(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public Response() {}

    // 返回404
    public void r404() {
        isNull();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        response.headers().set("Content-Type", "text/html;");
        write(response);
    }

    // 返回html
    public void writeHtml(String htmlUrl, Map<String, Object> msg) {
        try {
            isNull();
            InputStream in = PathConfig.class.getResourceAsStream("/" + htmlUrl);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s = bufferedReader.readLine();
                if (s == null) {
                    break;
                }
                for (Map.Entry<String, Object> entry : msg.entrySet()) {
                    String r = "{" + entry.getKey() + "}";
                    s = s.replace(r, entry.getValue() instanceof String ?
                            (String) entry.getValue()
                            : new Gson().toJson(entry.getValue()));
                }
                sb.append(s);
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(sb.toString().getBytes(CharsetUtil.UTF_8.name())));
            response.headers().set("Content-Type", "text/html; charset=utf-8");
            write(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 返回json
    public void writeJson(Map<String, Object> msg) throws Exception {
        Gson gson = new Gson();
        String s = gson.toJson(msg);
        isNull();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(s.getBytes(CharsetUtil.UTF_8.name())));
        response.headers().set("Content-Type", "application/json; charset=utf-8");
        write(response);
    }

    private void write(FullHttpResponse response) {
        channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.close();
    }

    private void isNull() {
        if (channelHandlerContext == null) {
            throw new RuntimeException("通道上下文不存在");
        }
    }

}
