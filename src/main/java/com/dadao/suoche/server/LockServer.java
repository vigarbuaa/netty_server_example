package com.dadao.suoche.server;

import com.dadao.suoche.server.handler.LoginRequestHandler;
import com.dadao.suoche.server.handler.PacketDecoder;
import com.dadao.suoche.server.handler.PacketEncoder;
import com.dadao.suoche.server.handler.Spliter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class LockServer {
	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChildChannelHandler())//
					.option(ChannelOption.SO_BACKLOG, 1024) //
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
			ChannelFuture f = b.bind(port).sync(); // (7)
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	/** 
	 */
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new Spliter());
			ch.pipeline().addLast(new PacketDecoder());
			ch.pipeline().addLast(new LoginRequestHandler());
			// ch.pipeline().addLast(new AuthHandler());
			// ch.pipeline().addLast(new MessageRequestHandler());
			ch.pipeline().addLast(new PacketEncoder());
		}
	}

	public static void main(String[] args) throws Exception {
		new LockServer().bind(8000);
	}
}