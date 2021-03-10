package com.gxk;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;


/**
 * netty自定义处理类
 * @author gaoXiangKang
 * @date 2021-03-10
 */
public class TomcatHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        Request boRequest = new Request(msg);
        Response boResponse = new Response(ctx);
        System.out.println("用户: " + ctx.channel().remoteAddress() + " 调用: " + boRequest.getUri().split("\\?")[0] + " 方法,请求方式为: " + boRequest.getMethod());
        DistributionServer distributionServer = new DistributionServer();
        distributionServer.process(boRequest, boResponse);
        System.out.println("调用成功。。。。。");
    }

}
