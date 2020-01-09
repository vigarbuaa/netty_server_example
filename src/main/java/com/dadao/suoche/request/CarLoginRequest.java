
package com.dadao.suoche.request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.dadao.suoche.exception.IllegelPackageException;
import com.dadao.suoche.protocol.BaseMessage;
import com.dadao.suoche.protocol.MsgHeader;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆登入注册消息包类
 */
@Data
@NoArgsConstructor
public class CarLoginRequest extends BaseMessage {
	
	private static Logger logger = Logger.getLogger(CarLoginRequest.class);
	
	public static final int MIN_MSG_LENGTH =  30;
	private Date dateTime;// 注册时间
	private int loginID = 0;// 登入流水号
	private String ICCID;// SIM卡ICCID号
	private short batTeamCount;// 可充电储能子系统数
	private byte batTeamCodeLength;// 可充电储能子系统编码长度
	private List<String> batTeamCodeList = new ArrayList<String>();// 可充电储能系统编码

	@Override
	public Object initFromByteBuf(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub
		super.initFromByteBuf(buf);
		MsgHeader header = this.getHeader();
		if(header.getAttachmentLength()!=0){
			logger.debug("bytes left is: "+buf.readableBytes());
			if (buf.readableBytes() < MIN_MSG_LENGTH) {
				logger.debug("登录包长度："+buf.readableBytes());
				throw new IllegelPackageException("登录命令包长度过小");
			}			
		// 数据单元解密
		byte encryption = header.getEncryption();
		ByteBuf newBuf;
		if (encryption == (byte) 0x02) {// RSA
			throw new IllegelPackageException("目前不支持RSA加密");
			// todu newBuf存储解密后的数据
		} else {
			newBuf = buf;
		}
		// 注册时间
		int year = newBuf.readByte() + 2000;
		int mon = newBuf.readByte()-1;
		int date = newBuf.readByte();
		int hour = newBuf.readByte();
		int min = newBuf.readByte();
		int sec = newBuf.readByte();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, mon, date, hour, min, sec);
		this.setDateTime(calendar.getTime());
		// 注册id
		this.setLoginID(newBuf.readUnsignedShort());
		byte[] iccid=new byte[20];
		newBuf.readBytes(iccid, 0, 20);
		this.setICCID(new String(iccid));
		this.setBatTeamCount(newBuf.readUnsignedByte());
		this.setBatTeamCodeLength(newBuf.readByte());
		//可充电储能系统编码
		byte[] btCode=new byte[batTeamCodeLength];
		for(int i=0;i<batTeamCount;i++){
			newBuf.readBytes(btCode, 0, batTeamCodeLength);
			batTeamCodeList.add(new String(btCode));
		}
		}
		return this;
	}

	@Override
	public void toNetByteBuf(ByteBuf buf) {
		// TODO Auto-generated method stub
		    super.toNetByteBuf(buf);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTime);
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
			buf.writeShort(loginID);
			buf.writeBytes(ICCID.getBytes());
			buf.writeByte(batTeamCount);
			buf.writeByte(batTeamCodeLength);
			for(int i=0;i<batTeamCount;i++){
				buf.writeBytes(batTeamCodeList.get(i).getBytes());
			}
	}
}