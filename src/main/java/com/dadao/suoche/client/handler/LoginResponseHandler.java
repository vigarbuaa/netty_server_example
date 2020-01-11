package com.dadao.suoche.client.handler;

import java.util.Date;
import java.util.UUID;

import com.dadao.suoche.request.LoginRequestPacket;
import com.dadao.suoche.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("LoginResponseHandler channelActive");
		LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");
    	System.out.println("LoginResponseHandler channelActive"+loginRequestPacket.toString() );
		ctx.channel().writeAndFlush(loginRequestPacket);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
    	SessionUtil.unBindSession(ctx.channel());
    }
    
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("LoginResponseHandler messageReceived: " + msg.getReason());
		if (msg.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            System.out.println("recv msg: " + msg.toString());
//            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + msg.getReason());
        }
	}
}