package com.dadao.suoche.server.handler;

import com.dadao.vms.datacollector.Config;
import com.dadao.vms.datacollector.KafkaMessage;
import com.dadao.vms.datacollector.bjmessage.requests.HeartBeatRequest;
import com.dadao.vms.datacollector.bjmessage.responses.CommonResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatRequest> {

	public HeartBeatHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HeartBeatRequest msg) throws Exception {
		// TODO Auto-generated method stub
		if(msg.getHeader().getRespID()!=0x01){
			// 发送回令
			CommonResponse resp = new CommonResponse(msg.getHeader(),(byte) 0x01);
			ctx.channel().writeAndFlush(resp);
			
			String topic = Config.topicMap.get("heartbeat");
			String key = msg.getHeader().getVehicleId();
			String value = msg.toJsonString();
			KafkaMessage km = new KafkaMessage(topic, key, value);
			ctx.fireChannelRead(km);
		}else{
			ctx.fireChannelRead(msg);
		}
		
	}

	/** 
     * 一段时间未进行读写操作 回调 ,用于检测心跳，一段时间未检测到心跳主动断开连接
     */  
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)  
            throws Exception {  
        // TODO Auto-generated method stub  
        super.userEventTriggered(ctx, evt);  
  
        if (evt instanceof IdleStateEvent) {  
  
            IdleStateEvent event = (IdleStateEvent) evt;  
              
            if (event.state().equals(IdleState.READER_IDLE)) {  
                //未进行读操作  
                System.out.println("READER_IDLE");  
                // 超时关闭channel  
                 ctx.close();  
          }   
        }  
    }  
}
