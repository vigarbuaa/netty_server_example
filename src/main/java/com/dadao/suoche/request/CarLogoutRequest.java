/**
 * @version 1.1
 */
package com.dadao.suoche.request;

import java.util.Calendar;
import java.util.Date;

import com.dadao.suoche.protocol.BaseMessage;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class CarLogoutRequest extends BaseMessage {

	private Date dateTime;// 登出时间
	private int logoutID;// 登出流水号

	@Override
	public Object initFromByteBuf(ByteBuf buf) throws Exception {
		super.initFromByteBuf(buf);
		// 登出时间
		int year = buf.readByte() + 2000;
		int mon = buf.readByte() - 1;
		int date = buf.readByte();
		int hour = buf.readByte();
		int min = buf.readByte();
		int sec = buf.readByte();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, mon, date, hour, min, sec);
		this.setDateTime(calendar.getTime());
		// 登出id
		this.setLogoutID(buf.readUnsignedShort());
		return this;
	}

	@Override
	public void toNetByteBuf(ByteBuf buf) {
		super.toNetByteBuf(buf);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		int year = calendar.get(Calendar.YEAR) - 2000;
		int mon = calendar.get(Calendar.MONTH) + 1;
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
		buf.writeShort(logoutID);
	}
}