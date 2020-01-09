package com.dadao.suoche.protocol;

import java.util.HashMap;
import java.util.Map;

import com.dadao.suoche.request.LoginRequestPacket;
import com.dadao.suoche.response.LoginResponsePacket;
import com.dadao.suoche.serialize.Serializer;
import com.dadao.suoche.serialize.impl.JSONSerializer;

import io.netty.buffer.ByteBuf;

public class PacketCodeC {
	public static final int MAGIC_NUMBER = 0x12345678;
	public static final PacketCodeC INSTANCE = new PacketCodeC();

	private final Map<Byte, Class<? extends Packet>> packetTypeMap;
	private final Map<Byte, Serializer> serializerMap;

	private PacketCodeC() {
		packetTypeMap = new HashMap<>();
		packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
		packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);

		serializerMap = new HashMap<>();
		Serializer serializer = new JSONSerializer();
		serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
	}

	public void encode(ByteBuf byteBuf, Packet packet) {
		byte[] bytes = Serializer.DEFAULT.serialize(packet);

		// 3. 实际编码过程
		byteBuf.writeInt(MAGIC_NUMBER);
		byteBuf.writeByte(packet.getVersion());
		byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());
		byteBuf.writeByte(packet.getCommand());
		byteBuf.writeInt(bytes.length);
		byteBuf.writeBytes(bytes);
	}

	public Packet decode(ByteBuf byteBuf) {
		byteBuf.skipBytes(4);
		byteBuf.skipBytes(1);
		// 序列化算法
		byte serializeAlgorithm = byteBuf.readByte();
		// 指令
		byte command = byteBuf.readByte();

		int length = byteBuf.readInt();
		byte[] bytes = new byte[length];
		byteBuf.readBytes(bytes);

		Class<? extends Packet> requestType = getRequestType(command); // 返回class
		Serializer serizlizer = getSerializer(serializeAlgorithm);

		if (requestType != null && serizlizer != null) {
			return serizlizer.deserialize(requestType, bytes);
		}
		return null;
	}

	private Serializer getSerializer(byte serializeAlgorithm) {
		return serializerMap.get(serializeAlgorithm);
	}

	private Class<? extends Packet> getRequestType(byte command) {
		return packetTypeMap.get(command);
	}
}