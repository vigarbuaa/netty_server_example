/**
 * 
 */
package com.dadao.suoche.protocol;

import java.util.HashMap;
import java.util.Map;

import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.request.CarLogoutRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: MessageIDs
 * @Description: 消息解包的时候 通过命令ID路由得到序列化反序列化要用到的消息类结构体
 * @author hefeng
 * @date 2016年3月30日
 *
 */
@Data
@NoArgsConstructor
public class MessageType {

	private static Map<Byte, Class<? extends BaseMessage>> ID_MAP = new HashMap<Byte, Class<? extends BaseMessage>>();
	
	public static final byte LOGIN = 0x01;
	public static final byte LOGOUT = 0x04;

	static {
		ID_MAP.put((byte) 0x01, CarLoginRequest.class);
		ID_MAP.put((byte) 0x04, CarLogoutRequest.class);
	}

	public static boolean hasID(byte id) {
		if (ID_MAP.containsKey(id)) {
			return true;
		} else {
			return false;
		}
	}

	public static Class<? extends BaseMessage> getMessageClass(byte id) {
		return ID_MAP.get(id);
	}
}