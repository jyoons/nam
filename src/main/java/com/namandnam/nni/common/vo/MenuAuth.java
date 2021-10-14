package com.namandnam.nni.common.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("MenuAuth")
public class MenuAuth {
    
	String code;
	String name;
	String link;
	int auth;
	
}
