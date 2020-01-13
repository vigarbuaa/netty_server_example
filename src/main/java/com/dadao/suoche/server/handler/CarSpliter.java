package com.dadao.suoche.server.handler;

import java.nio.ByteOrder;

import org.apache.log4j.Logger;

import com.dadao.suoche.protocol.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class CarSpliter extends LengthFieldBasedFrameDecoder {
	private static Logger logger = Logger.getLogger(CarSpliter.class);
	public CarSpliter() {
//		 super(ByteOrder.BIG_ENDIAN, BaseMessage.MAX_MSG_LENGTH,BaseMessage.LENGTH_OFFSET, BaseMessage.LENGTH_SZIE, 1, 0, false);
		 super(ByteOrder.BIG_ENDIAN, BaseMessage.MAX_MSG_LENGTH,BaseMessage.LENGTH_OFFSET, BaseMessage.LENGTH_SZIE, 1, 0, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// 加逻辑, 如果magic number不对，则退出?
		logger.debug("the message length is: " + in.readableBytes());
		logger.debug("--------spliter print raw data----------");
		for (int i = 0; i < in.readableBytes();) {
			String rawData = "";
			for (int j = 0; j < 10 && i < in.readableBytes(); i++) {
				rawData += " Ox"+Integer.toString((in.getByte(i) & 0xff) + 0x100, 16).substring(1);
			}
			logger.debug(" " + rawData);
		}
		return super.decode(ctx, in);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("come to spliter channelActive");
	}
}