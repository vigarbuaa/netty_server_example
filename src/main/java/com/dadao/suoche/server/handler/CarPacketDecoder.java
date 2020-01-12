package com.dadao.suoche.server.handler;

import java.nio.ByteOrder;
import java.util.List;

import org.apache.log4j.Logger;

import com.dadao.suoche.exception.IllegelPackageException;
import com.dadao.suoche.protocol.BaseMessage;
import com.dadao.suoche.protocol.MessageType;
import com.dadao.suoche.protocol.MsgHeader;
import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.response.CommonResponse;
import com.dadao.suoche.util.MockUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;

public class CarPacketDecoder extends ByteToMessageDecoder {
	private static Logger logger = Logger.getLogger(CarPacketDecoder.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("come to PacketDecoder channelActive");
//		CarLoginRequest msg=MockUtil.mockLogin();
//		ctx.channel().writeAndFlush(msg);
//		System.out.println("flag is " + msg.toJsonString());
//		System.out.println("send car login request msg successed");
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		logger.info("Come to PacketDecoder! ");

		byte reqID = buf.getByte(BaseMessage.TYPE_POS);
		byte respID = buf.getByte(BaseMessage.TYPE_POS + 1);
		byte[] vinBytes = new byte[17];
		buf.getBytes(4, vinBytes, 0, 17);
		String key = new String(vinBytes);
		logger.debug("decoder vehicleId : " + key);
		MsgHeader header = MsgHeader.getCommonHeader(reqID, respID, key, 31);
		AttributeKey<MsgHeader> bufKey = AttributeKey.valueOf("head");
		ctx.attr(bufKey).set(header);

		// 判断包长度
		int msgLength = buf.readableBytes();
		logger.debug("判断包长度 readableBytes length is :" + msgLength);
		if (msgLength < BaseMessage.BASE_MSG_LENGTH) {
//			throw new IllegelPackageException("包长度过小");
			return;
		}
		// 判断校验码
		int checkZero = 0;
		for (int i = 0; i < msgLength; i++) {
			checkZero ^= buf.getByte(i);
		}
		logger.debug("checkzero result: (" + checkZero + ")");
		logger.debug("---------------------------------");
		if (checkZero != 0) {
			logger.error("checkzero failed");
			throw new IllegelPackageException("校验错误");
		}

		logger.info("checkzero success!");

		if (!MessageType.hasID(reqID)) {
			logger.error("typeId wrong!");
			String errorStr = "命令字错误:" + Integer.toHexString(reqID);
			throw new IllegelPackageException(errorStr + key);
		}
		logger.info("typeId right!");
		ByteBuf bigEndianBuf = buf.order(ByteOrder.BIG_ENDIAN);

		BaseMessage msg = buildMessage(reqID, respID, bigEndianBuf);

		int checkCode = bigEndianBuf.readByte();// 读出校验码;
		if (msg != null) {
			System.out.println("WebCore msgDecoder debug: " + msg.toJsonString());
			out.add(msg);
		}
		System.out.println("----Server decodder End----------");
	}

	protected BaseMessage buildMessage(byte reqID, byte respID, ByteBuf buf)
			throws InstantiationException, IllegalAccessException, Exception {
		if (respID != (byte) 0xfe) {
			CommonResponse msg = new CommonResponse();
			return (BaseMessage) msg.initFromByteBuf(buf);
		} else {
			return (BaseMessage) MessageType.getMessageClass(reqID).newInstance().initFromByteBuf(buf);
		}
	}
}