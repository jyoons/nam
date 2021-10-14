package com.namandnam.nni.common.constant;

public enum TableCategoryEnum {
	
	//게시판
	BOARD("BOARD"),
	
	//TASK
	TASK("TASK");
	
	
	private String code;
	
	TableCategoryEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
}
