/**
 * 
 */
package com.dadao.suoche.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.netty.buffer.ByteBuf;

public interface NetSerializable {

	public void toNetByteBuf(ByteBuf buf);

	public Object initFromByteBuf(ByteBuf buf) throws Exception;

	public String toJsonString() throws JsonProcessingException;

}