package com.dadao.suoche.server.handler;

import com.dadao.suoche.protocol.Packet;
import com.dadao.suoche.protocol.PacketCodeC;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
		PacketCodeC.INSTANCE.encode(out, msg); 
	}
}