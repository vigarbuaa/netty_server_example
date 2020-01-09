package com.dadao.suoche.client;

import java.util.Date;
import java.util.Scanner;

import com.dadao.suoche.client.handler.LoginResponseHandler;
import com.dadao.suoche.protocol.MsgHeader;
import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.server.handler.PacketDecoder;
import com.dadao.suoche.server.handler.PacketEncoder;
import com.dadao.suoche.server.handler.Spliter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class simClient {
	private static final int MAX_RETRY = 5;
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 8000;

	public static void main(String[] args) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
//						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(new LoginResponseHandler());
						ch.pipeline().addLast(new PacketEncoder());
						
//						ch.pipeline().addLast(new Spliter());
						// ch.pipeline().addLast(new MessageResponseHandler());
						// ch.pipeline().addLast(new ClientHandler());
					}
				});
		connect(bootstrap, HOST, PORT, MAX_RETRY);
	}

	private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
		System.out.println("begin to connect retry is " + retry);

		ChannelFuture c;
		try {
			c = bootstrap.connect(HOST, PORT).sync();
//			startConsoleThread(c.channel());
			c.channel().closeFuture().sync();
			bootstrap.group().shutdownGracefully();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void waitForLoginResponse() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignored) {
		}
	}

	private static void startConsoleThread(Channel channel) {
		System.out.println("start console thread");
		Scanner sc = new Scanner(System.in);
		new Thread(() -> {
			while (!Thread.interrupted()) {
				// if (!SessionUtil.hasLogin(channel)){
				System.out.println("write something");
				String username = sc.nextLine();
				CarLoginRequest carLoginRequest = new CarLoginRequest();
				carLoginRequest.setDateTime(new Date());
				carLoginRequest.setICCID("1111ll");
				carLoginRequest.setLoginID(22);
				carLoginRequest.setBatTeamCount((short) 1);
				carLoginRequest.setBatTeamCount((short) 2);
				MsgHeader header = MsgHeader.getCommonHeader((byte) 0xfe, (byte) 0xfe, "1111", 36);
				carLoginRequest.setHeader(header);
				channel.writeAndFlush(carLoginRequest);
				waitForLoginResponse();
				// }else{
				/*
				 * String toUserId=sc.next(); String message=sc.next();
				 * MessageRequestPacket packet = new MessageRequestPacket();
				 * packet.setToUserId(toUserId); packet.setMessage(message);
				 * channel.writeAndFlush(packet);
				 */
				// }
			}
		}).start();
	}
}
