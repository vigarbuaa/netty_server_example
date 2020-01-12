package com.dadao.suoche.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dadao.suoche.attr.Attributes;
import com.dadao.suoche.attr.Session;
import com.dadao.suoche.protocol.MsgHeader;
import com.dadao.suoche.request.CarLoginRequest;
import com.dadao.suoche.request.HeartBeatRequest;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class MockUtil {
	
	public static HeartBeatRequest mockHeartBeat() {
		HeartBeatRequest heart = new HeartBeatRequest();
		MsgHeader header = MsgHeader.getCommonHeader((byte) 0x07, (byte) 0xfe, "HEARTGAWXJ1061315", 1);
		heart.setHeader(header);
		return heart;
	}

	public static CarLoginRequest mockLogin() {
		CarLoginRequest carLoginRequest = new CarLoginRequest();
		carLoginRequest.setICCID("123456789ABCDEF12345");
		carLoginRequest.setLoginID(2);
		carLoginRequest.setBatTeamCount((short) 3);
		carLoginRequest.setDateTime(new Date());
		List<String> teamList = new ArrayList<String>();
		teamList.add("head1");
		teamList.add("head2");
		teamList.add("head3");
		carLoginRequest.setBatTeamCodeList(teamList);
		MsgHeader header = MsgHeader.getCommonHeader((byte) 0x01, (byte) 0xfe, "LZYTFGAWXJ1061315", 36);
		carLoginRequest.setHeader(header);
		return carLoginRequest;
	}
}