package com.jkp.mp3tag;

public enum STATUS {
	FAIL("Fail"),
	SUCCESS("Success");
	private String desc;
	private STATUS(String desc) {
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}
}
