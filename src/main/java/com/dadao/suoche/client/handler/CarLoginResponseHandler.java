package com.dadao.suoche.client.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dadao.suoche.protocol.MsgHeader;
import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.response.CommonResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CarLoginResponseHandler extends SimpleChannelInboundHandler<CommonResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("come to CarLoginResponseHandler!");
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
		System.out.println("flag is " + carLoginRequest.toJsonString());
		System.out.println("send car login request msg successed");
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