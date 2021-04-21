package com.example.exam.common.exception;

public class ServiceException extends Exception {
	private String retCd;
	private String retMsg;
	
	public ServiceException() {}

	public ServiceException(String retCd, String retMsg) {
		this.retCd = retCd;
		this.retMsg = retMsg;
	}
	
	public String getRtCd() {
		return retCd;
	}

	@Override
	public String getMessage() {
		return retMsg;
	}
}
