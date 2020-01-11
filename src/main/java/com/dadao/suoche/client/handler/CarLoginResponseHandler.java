package com.dadao.suoche.client.handler;

import org.apache.log4j.Logger;

import com.dadao.suoche.response.CommonResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CarLoginResponseHandler extends SimpleChannelInboundHandler<CommonResponse> {
	private static Logger logger = Logger.getLogger(CarLoginResponseHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("come to CarLoginResponseHandler!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		// SessionUtil.unBindSession(ctx.channel());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, CommonResponse msg) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("收到回令：LoginResponseHandler CommonResponse messageReceived: " + msg.toJsonString());
		
	}
}