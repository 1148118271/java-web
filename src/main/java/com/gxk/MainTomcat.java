package com.gxk;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 主启动类
 * @author gaoXiangKang
 * @date 2021-03-10
 */
public class MainTomcat {

    private static final int DEF_PORT = 8080;

    public static void start() throws Exception {
        MainTomcat.start(DEF_PORT);
    }

    public static void start(int port) throws Exception {
        // 获取配置
        PathConfig.initUri();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // HttpServerCodec http编解码，请求解码 返回编码
                                // 等同 HttpRequestDecoder HttpResponseEncoder
                                .addLast(new HttpServerCodec())
                                // HttpObjectAggregator 可以接收http请求body参数
                                .addLast(new HttpObjectAggregator(512 * 1024))
                                // 自定义处理
                                .addLast(new TomcatHandler());
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
