package com.dadao.suoche.exception;

import com.dadao.suoche.protocol.MsgHeader;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IllegelPackageException extends Exception {
	
	private String vin;

	public IllegelPackageException(String message) {
		super(message);
	}

	public IllegelPackageException(Throwable cause) {
		super(cause);
	}
	
	public IllegelPackageException(String message,String vin) {
		super(message);
		this.vin=vin;
	}

	public IllegelPackageException(Throwable cause,String vin) {
		super(cause);
		this.vin=vin;
	}

	public IllegelPackageException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegelPackageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}