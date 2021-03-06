/**
 * 
 */
package com.dadao.suoche.server.handler;

import org.apache.log4j.Logger;

import com.dadao.suoche.attr.Session;
import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.response.CommonResponse;
import com.dadao.suoche.util.SessionUtil;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CarLoginRequestHandler extends SimpleChannelInboundHandler<CarLoginRequest> {

	private static Logger logger = Logger.getLogger(CarLoginRequestHandler.class);
	// private static List<String> tidList = new ArrayList<String>();
	// public static Map<String,CarChannelBean> vehicleMap=new
	// ConcurrentHashMap<String,CarChannelBean>();
	// public static Map<String,String> vidIccidMap=new
	// ConcurrentHashMap<String,String>();
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("come to LoginRequestHandler channelActive");
	}
	
	protected void messageReceived(ChannelHandlerContext ctx, CarLoginRequest msg) throws Exception {
		logger.debug("LoginRequestHandler messageReceived");
		CommonResponse resp;
//		String value = msg.toJsonString();
//		logger.info(iccid + " | " + key + " | " + value);
//		// 程序自己判断一下返回什么值，这里做个示例
		resp = new CommonResponse(msg.getHeader(), (byte) 0x01);
		ctx.fireChannelRead(msg);
		logger.debug("LoginRequestHandler begin to send resp");
		sendResp(resp, ctx, msg);
		SessionUtil.bindSession(new Session("11213", msg.getICCID() ), ctx.channel());
		logger.debug("LoginRequestHandler finish send resp");
	}

	// 发送回令
	protected void sendResp(CommonResponse resp, ChannelHandlerContext ctx, final CarLoginRequest msg)
			throws InterruptedException {
		ctx.channel().writeAndFlush(resp).sync().addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if (future.isDone() && future.isSuccess()) {
					logger.info("Success!! Login_Stat(" + msg.getHeader().getVehicleId() + "): " + "isDone="
							+ future.isDone() + "  isSuccess=" + future.isSuccess() + "  So we login Success!!");
				} else {
					logger.info("Failed!! Login_Stat(" + msg.getHeader().getVehicleId() + "): " + "isDone="
							+ future.isDone() + "  isSuccess=" + future.isSuccess() + "  So we login Failed!!");
				}
			}
		});
	}

	protected void buildPipe(ChannelHandlerContext ctx, CarLoginRequest msg) {
		// 增加其他handler
	}
}