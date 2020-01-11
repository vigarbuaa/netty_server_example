package com.dadao.suoche.server.handler;

import org.apache.log4j.Logger;
import com.dadao.suoche.request.HeartBeatRequest;
import com.dadao.suoche.response.CommonResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatRequest> {
	private static Logger logger = Logger.getLogger(CarLoginRequestHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HeartBeatRequest msg) throws Exception {
		if(msg.getHeader().getRespID()!=0x01){
			// 发送回令
			CommonResponse resp = new CommonResponse(msg.getHeader(),(byte) 0x01);
			logger.debug("heartBeat resp: " + resp.toJsonString());
			ctx.channel().writeAndFlush(resp);
		}else{
			logger.error("get HeartRequest respID error: " + msg.getHeader().getRespID());
		}
	}
}