package com.dadao.suoche.request;

import com.dadao.suoche.protocol.Command;
import com.dadao.suoche.protocol.Packet;

import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {
	private String userId;
	private String username;
	private String password;

	@Override
	public Byte getCommand() {
		return Command.LOGIN_REQUEST; 
	}
}