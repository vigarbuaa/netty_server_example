package com.dadao.suoche.server.handler;

import java.nio.ByteOrder;

import org.apache.log4j.Logger;

import com.dadao.suoche.protocol.BaseMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Spliter extends LengthFieldBasedFrameDecoder {

	private static Logger logger = Logger.getLogger(Spliter.class);

	public Spliter() {
//		super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
		super(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE,
				BaseMessage.LENGTH_OFFSET, BaseMessage.LENGTH_SZIE, 1, 0, false);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		//打印原始报文
		logger.debug("the message length is: " + in.readableBytes());
		logger.debug("--------spliter print raw data----------");
		for (int i = 0; i < in.readableBytes();) {
			String rawData = "";
			for (int j = 0; j < 10 && i < in.readableBytes(); i++) {
				rawData += " Ox"+Integer.toString((in.getByte(i) & 0xff) + 0x100, 16).substring(1);
			}
			logger.debug(" " + rawData);
		}
		
		// 如果首字节不对##，加退出逻辑
		/*
		if (in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER) {
			logger.error("magic number error,close conection");
			ctx.channel().close();
			return null;
		}
		*/
		return super.decode(ctx, in);
	}
}
