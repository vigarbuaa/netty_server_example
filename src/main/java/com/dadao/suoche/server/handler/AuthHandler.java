package com.dadao.suoche.server.handler;

import com.dadao.suoche.util.SessionUtil;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class AuthHandler extends ChannelHandlerAdapter {
	 public static final AuthHandler INSTANCE = new AuthHandler();
	 
	 public AuthHandler(){
	 }
	 
	 @Override
	 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(SessionUtil.hasLogin(ctx.channel())){
			System.err.println("Auth handler success");
			ctx.channel().pipeline().remove(this);
			ctx.fireChannelRead(msg);
		}else{
			// not log in
			System.err.println("not login msg");
			ctx.channel().close();
		}
	 }
}