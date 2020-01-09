package com.dadao.suoche.server.handler;

import java.nio.ByteOrder;
import com.dadao.suoche.protocol.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Spliter extends LengthFieldBasedFrameDecoder {

	public Spliter() {
		 super(ByteOrder.BIG_ENDIAN, BaseMessage.MAX_MSG_LENGTH,BaseMessage.LENGTH_OFFSET, BaseMessage.LENGTH_SZIE, 1, 0, false);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// 加逻辑, 如果magic number不对，则退出?
		System.out.println("come to spliter decoder");

		return super.decode(ctx, in);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("come to spliter channelActive");
	}
}