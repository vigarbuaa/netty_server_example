package com.dadao.suoche.server.handler;

import com.dadao.suoche.request.LoginRequestPacket;
import com.dadao.suoche.response.LoginResponsePacket;
import com.dadao.suoche.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
		System.out
				.println("LoginRequestHandler messageReceived msg info: " + msg.getUserId() + "  " + msg.getPassword());
		LoginResponsePacket login_rsp = new LoginResponsePacket();
		login_rsp.setSuccess(true);
		login_rsp.setReason("all money go my home");
		ctx.channel().writeAndFlush(login_rsp);
//		LoginUtil.markAsLogin(ctx.channel()); // mark login session
//		SessionUtil.bindSession(new Session(), ctx.channel());
	}
}