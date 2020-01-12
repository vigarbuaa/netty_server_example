package com.dadao.suoche.client;

import java.util.Scanner;

import com.dadao.suoche.client.handler.CarLoginResponseHandler;
import com.dadao.suoche.client.handler.HeartBeatClientHandler;
import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.server.handler.CarPacketDecoder;
import com.dadao.suoche.server.handler.CarPacketEncoder;
import com.dadao.suoche.server.handler.Spliter;
import com.dadao.suoche.util.MockUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
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
						ch.pipeline().addLast(new Spliter());
						// ch.pipeline().addLast(new PacketEncoder());
						// ch.pipeline().addLast(new LoginResponseHandler());
						ch.pipeline().addLast(new CarPacketDecoder());
						ch.pipeline().addLast(new HeartBeatClientHandler());
						ch.pipeline().addLast(new CarLoginResponseHandler());
						// ch.pipeline().addLast(new MessageResponseHandler());
						// ch.pipeline().addLast(new ClientHandler());
						ch.pipeline().addFirst(new CarPacketEncoder());
					}
				});
		connect(bootstrap, HOST, PORT, MAX_RETRY);
	}

	private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
		System.out.println("begin to connect retry is " + retry);

		ChannelFuture c;
		try {
			c = bootstrap.connect(HOST, PORT).sync();
			startConsoleThread(c.channel());
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
				System.out.println("输入指令:");
				String cmd = sc.nextLine();
				if (cmd.trim().equals("login")) {
					CarLoginRequest msg = MockUtil.mockLogin();
					channel.writeAndFlush(msg);
				}else{
					System.out.println("not login cmd?");
				}

				// if (!SessionUtil.hasLogin(channel)){
				// System.out.println("输入指令:");
				// String username = sc.nextLine();
				// LoginRequestPacket loginRequestPacket = new
				// LoginRequestPacket();
				// loginRequestPacket.setUsername(username);
				// System.out.println("[begin to send]:
				// "+loginRequestPacket.toString());
				// channel.writeAndFlush(loginRequestPacket);
				// waitForLoginResponse();
				// }else{
				// String toUserId=sc.next();
				// String message=sc.next();
				// MessageRequestPacket packet = new MessageRequestPacket();
				// packet.setToUserId(toUserId);
				// packet.setMessage(message);
				// channel.writeAndFlush(packet);
				// }
			}
		}).start();
	}
}
