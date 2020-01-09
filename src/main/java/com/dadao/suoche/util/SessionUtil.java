package com.dadao.suoche.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dadao.suoche.attr.Attributes;
import com.dadao.suoche.attr.Session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class SessionUtil {

	// userId -> channel的映射
	private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

	public static void bindSession(Session session, Channel channel) {
		userIdChannelMap.put(session.getUserId(), channel);
		channel.attr(Attributes.SESSION).set(session);
	}

	public static void unBindSession(Channel channel) {
		if (hasLogin(channel)) {
			String userId = getSession(channel).getUserId();
			userIdChannelMap.remove(userId);
			channel.attr(Attributes.SESSION).set(null);
		}
	}

	public static boolean hasLogin(Channel channel) {
		return channel.hasAttr(Attributes.SESSION);
	}

	public static Session getSession(Channel channel) {
		return channel.attr(Attributes.SESSION).get();
	}
	
	public static Channel getChannel(String userId){
		return userIdChannelMap.get(userId);
	}
}
