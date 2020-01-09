package com.dadao.suoche.protocol;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseMessage implements NetSerializable {
	public static final int MAX_MSG_LENGTH = Integer.MAX_VALUE;
	public static final int HEADER_LENGTH = 24;
	public static final int BASE_MSG_LENGTH = 25;// 包头长度加校验码
	public static final int LENGTH_OFFSET = 22;
	public static final int LENGTH_SZIE = 2;
	public static final int TYPE_POS = 2;
	private MsgHeader header;

	public BaseMessage(MsgHeader header) {
		this.setHeader(header);
	}

	public void toNetByteBuf(ByteBuf buf) {
		try {
			this.getHeader().toNetByteBuf(buf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object initFromByteBuf(ByteBuf buf) throws Exception {
		MsgHeader header = new MsgHeader();
		header.initFromByteBuf(buf);
		this.setHeader(header);
		return this;
	}

	public String toJsonString() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(fmt);
		String json = objectMapper.writeValueAsString(this);
		return json;
	}
}