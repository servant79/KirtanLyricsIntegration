package com.jkp.excel.utils;

import com.jkp.mp3tag.STATUS;

public class UpdateStatus {

	private STATUS status= STATUS.SUCCESS;
	private String message = status.getDesc();
	
	public STATUS getStatus() {
		return status;
	}
	public void setStatus(STATUS status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
