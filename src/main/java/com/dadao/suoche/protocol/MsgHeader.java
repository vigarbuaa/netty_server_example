package com.dadao.suoche.protocol;

import java.text.SimpleDateFormat;

import com.dadao.suoche.exception.IllegelPackageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MsgHeader implements NetSerializable {
	public static int HEADER_LENGTH = 12;
	private String flag;// 起始符
	private byte ReqID;// 命令标识
	private byte respID;// 应答标志
	private String vehicleId;// 车辆识别码
	private byte encryption;// 加密方式
	private int attachmentLength;// 数据长度

	public static MsgHeader getCommonHeader(byte reqID, byte respID, String vehicleID, int attachmentLength) {
		MsgHeader header = new MsgHeader();
		header.setFlag("##");
		header.setReqID(reqID);
		header.setRespID(respID);
		header.setVehicleId(vehicleID);
		header.setEncryption((byte) 1);
		header.setAttachmentLength(attachmentLength);
		return header;
	}

	public void toNetByteBuf(ByteBuf buf) {
		System.out.println("toNetByteBuf mark1");
		try {
		System.out.println("toNetByteBuf mark2");
			byte[] flag = this.getFlag().getBytes();
			buf.writeBytes(flag);
		System.out.println("toNetByteBuf mark3");
			// 写命令标识
			buf.writeByte(this.getReqID());
			// 写应答标志
			buf.writeByte(this.getRespID());
			// 写车辆识别码
			byte[] vehicleID = this.getVehicleId().getBytes();
			buf.writeBytes(vehicleID);
			for (int i = vehicleID.length; i < 17; i++) {
				buf.writeByte(0);
			}
			// 写加密方式
			buf.writeByte(this.getEncryption());
			// 写数据长度
			buf.writeShort(this.getAttachmentLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Object initFromByteBuf(ByteBuf buf) throws IllegelPackageException {
		// TODO Auto-generated method stub
		// 合法性判断
		// 判断是否小于最小包长度
		int msgLength = buf.readableBytes();
		if (msgLength < BaseMessage.HEADER_LENGTH) {
			throw new IllegelPackageException("包长度过小");
		}
		// 判断标志字
		byte flag[] = new byte[2];
		flag[0] = buf.readByte();
		flag[1] = buf.readByte();
		if ((flag[0] != 0x23) || (flag[1] != 0x23)) {
			throw new IllegelPackageException("标志字错误");
		}
		// 判断命令字
		byte reqID = buf.readByte();
		if (!MessageType.hasID(reqID)) {
			String errorStr = "命令字错误:" + Integer.toHexString(reqID);
			throw new IllegelPackageException(errorStr);
		}
		// 读回令字
		byte respID = buf.readByte();
		// 读车辆识别码
		byte[] vid = new byte[17];
		buf.readBytes(vid, 0, 17);
		// 读数据加密方式
		byte encryption = buf.readByte();
		// 判断数据单元长度
		int length = buf.readUnsignedShort();
		if (length != (buf.readableBytes() - 1)) {
			throw new IllegelPackageException("数据单元长度错误");
		}

		// 构造包头
		this.setFlag(new String(flag));
		this.setReqID(reqID);
		this.setRespID(respID);
		this.setVehicleId(new String(vid));
		this.setEncryption(encryption);
		this.setAttachmentLength(length);
		return this;
	}

	public String toJsonString() throws JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(fmt);
		String json = objectMapper.writeValueAsString(this);
		return json;
	}
}