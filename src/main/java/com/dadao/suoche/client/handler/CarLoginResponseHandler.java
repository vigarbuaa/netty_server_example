package com.dadao.suoche.client.handler;

import com.dadao.suoche.response.CommonResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CarLoginResponseHandler extends SimpleChannelInboundHandler<CommonResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("come to CarLoginResponseHandler!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		// SessionUtil.unBindSession(ctx.channel());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, CommonResponse msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("收到回令：LoginResponseHandler CommonResponse messageReceived: " + msg.toJsonString());
	}
}