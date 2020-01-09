/**
 * 
 */
package com.dadao.suoche.response;

import java.util.Calendar;
import java.util.Date;

import com.dadao.suoche.protocol.BaseMessage;
import com.dadao.suoche.protocol.MsgHeader;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class CommonResponse extends BaseMessage {
	
	private Date dateTime;
	
	public CommonResponse() {
		super();
	}
	
	public CommonResponse(MsgHeader header, byte respID) {
		super(header);
		header.setRespID(respID);
		header.setAttachmentLength(6);
	}	

	@Override
	public void toNetByteBuf(ByteBuf buf) {
		super.toNetByteBuf(buf);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		int year = calendar.get(Calendar.YEAR) - 2000;
		int mon = calendar.get(Calendar.MONTH)+1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		buf.writeByte(year);
		buf.writeByte(mon);
		buf.writeByte(date);
		buf.writeByte(hour);
		buf.writeByte(min);
		buf.writeByte(sec);
	}

	@Override
	public Object initFromByteBuf(ByteBuf buf) throws Exception {
		super.initFromByteBuf(buf);
		int year = buf.readByte() + 2000;
		int mon = buf.readByte()-1;
		int date = buf.readByte();
		int hour = buf.readByte();
		int min = buf.readByte();
		int sec = buf.readByte();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, mon, date, hour, min, sec);
		this.setDateTime(calendar.getTime());
		return this;
	}
}