package com.dadao.suoche.client.handler;

import org.apache.log4j.Logger;
import com.dadao.suoche.request.HeartBeatRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<HeartBeatRequest> {
	// 参数中由于HeartBeatRequest/HeartBeatRespone结构相同，这里就互相替代了
	private static Logger logger = Logger.getLogger(HeartBeatClientHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("HeartBeatClientHandler channelActive");
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HeartBeatRequest msg) throws Exception {
		logger.debug("get heartBeat Response: " + msg.toJsonString());
	}
}