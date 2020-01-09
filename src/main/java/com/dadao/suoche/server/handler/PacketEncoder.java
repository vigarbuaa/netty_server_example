package com.dadao.suoche.server.handler;

import java.nio.ByteOrder;

import org.apache.log4j.Logger;

import com.dadao.suoche.protocol.BaseMessage;
import com.sun.xml.internal.ws.api.message.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PacketEncoder extends MessageToByteEncoder<BaseMessage> {
	private static Logger logger = Logger.getLogger(PacketEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, BaseMessage msg, ByteBuf out) throws Exception {
		BaseMessage baseMessage = (BaseMessage) msg;
		ByteBuf bigEndianBuf = out.order(ByteOrder.BIG_ENDIAN);
		baseMessage.toNetByteBuf(bigEndianBuf);
		System.out.println("[readableBytes]"+bigEndianBuf.readableBytes());
		bigEndianBuf.setShort(22, bigEndianBuf.readableBytes() - 24);
		if (bigEndianBuf.readableBytes() > 0) {
			addCheckCode(bigEndianBuf);
		}
		System.out.println("the message length is: " + bigEndianBuf.readableBytes());
		System.out.println("ENCODER RAW DATA:----------------------");
		for (int i = 0; i < bigEndianBuf.readableBytes();) {
			String rawData = "";
			for (int j = 0; j < 10 && i < bigEndianBuf.readableBytes(); i++) {
				rawData += Integer.toString((bigEndianBuf.getByte(i) & 0xff) + 0x100, 16).substring(1);
				rawData += "   ";
			}
			System.out.println(" " + rawData);
		}
		System.out.println("ENCODER RAW DATA:----------------------");
	}

	private void addCheckCode(ByteBuf buf) {
		buf.markReaderIndex();
		byte checkCode = 0;
		int x = buf.readableBytes();
		int n = buf.readableBytes();
		for (int i = 0; i < n; i++) {
			checkCode ^= buf.readByte();
		}
		buf.writeByte(checkCode);
		buf.resetReaderIndex();
	}
}