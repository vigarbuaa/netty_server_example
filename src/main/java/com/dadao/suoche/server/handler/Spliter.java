package com.dadao.suoche.server.handler;

import com.dadao.suoche.protocol.PacketCodeC;

import org.apache.log4j.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Spliter extends LengthFieldBasedFrameDecoder {
	private static final int LENGTH_FIELD_OFFSET = 7;
	private static final int LENGTH_FIELD_LENGTH = 4;

	private static Logger logger = Logger.getLogger(Spliter.class);

	public Spliter() {
		super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		//打印原始报文
		logger.debug("the message length is: " + in.readableBytes());
		logger.debug("RAW DATA RECORDER:----------------------");
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
