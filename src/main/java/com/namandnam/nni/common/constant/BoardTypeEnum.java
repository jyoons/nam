package com.namandnam.nni.common.constant;

public enum BoardTypeEnum {
	
	//공지사항
	NOTICE("00"),
	
	//FAQ
	FAQ("01"),
	
	//자료실
	DATA_ROOM("02"),
	
	//새소식
	NEWS("03");
	
	private String code;
	
	BoardTypeEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static boolean isValid(String code) {
		boolean result = false;
		for(BoardTypeEnum value : values()) {
			if(value.getCode().equals(code)) {
				result = true;
				break;
			}
		}
		return result;
	}
}
