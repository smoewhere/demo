package org.fan.demo.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.fan.demo.channelhandler.MyServerChannelHandler;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.8 18:48
 */
public class Server {

    public void init(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        new ServerBootstrap().group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
        .childHandler(new MyServerChannelHandler()).option(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
