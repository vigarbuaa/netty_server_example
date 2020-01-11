package com.dadao.suoche.request;

import com.dadao.suoche.protocol.BaseMessage;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartBeatRequest extends BaseMessage{

	public Object initFromByteBuf(ByteBuf buf) throws Exception {
		super.initFromByteBuf(buf);
		return this;
	}

	@Override
	public void toNetByteBuf(ByteBuf buf) {
		super.toNetByteBuf(buf);
	}	
}