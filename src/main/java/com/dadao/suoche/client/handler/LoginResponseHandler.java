package com.dadao.suoche.client.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dadao.suoche.protocol.MsgHeader;
import com.dadao.suoche.request.CarLoginRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<CarLoginRequest> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("come to LoginResponseHandler!");
		CarLoginRequest carLoginRequest = new CarLoginRequest();
		carLoginRequest.setICCID("123456789ABCDEF12345");
		carLoginRequest.setLoginID(2);
		carLoginRequest.setBatTeamCount((short) 3);
		carLoginRequest.setDateTime(new Date());
		List<String> teamList =new ArrayList<String>();
		teamList.add("head1");
		teamList.add("head2");
		teamList.add("head3");
		carLoginRequest.setBatTeamCodeList(teamList);
		MsgHeader header = MsgHeader.getCommonHeader((byte) 0x01, (byte) 0xfe, "2222", 36);
		carLoginRequest.setHeader(header);
		ctx.channel().writeAndFlush(carLoginRequest);
	/*	.addListener(
				future->{
					if (future.isSuccess()){
						System.out.println("发送成功");
					}else{
						System.out.println(future.cause().getCause().toString());
						System.out.println(future.cause().getStackTrace().toString());
						future.cause().printStackTrace();
						System.out.println(future.cause().getStackTrace().toString());
						System.out.println("没搞定");
					}
				}
			);
	*/
		
		System.out.println("flag is " + carLoginRequest.toJsonString());
		System.out.println("send msg successed");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		// SessionUtil.unBindSession(ctx.channel());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, CarLoginRequest msg) throws Exception {
		System.out.println("收到回令：LoginResponseHandler messageReceived: " + msg.toJsonString());
		// 判断并打印回令信息
	}
}