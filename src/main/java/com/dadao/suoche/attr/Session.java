package com.dadao.suoche.attr;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Session {
	private String userId;
	private String iccid;

    public Session(String userId, String userName) {
        this.userId = userId;
        this.iccid = userName;
    }
    
	@Override
	public String toString(){
		return userId+":"+iccid;
	}
}
