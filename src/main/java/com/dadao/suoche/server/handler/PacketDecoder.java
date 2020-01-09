package com.dadao.suoche.server.handler;

import java.util.List;

import com.dadao.suoche.protocol.PacketCodeC;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("come to PacketDecoder decode");
		out.add(PacketCodeC.INSTANCE.decode(in));
	}
}