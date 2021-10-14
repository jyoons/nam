package com.namandnam.nni.common.vo;

import java.util.Map;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("Roles")
public class Roles {
    
	 String idx;
	 String roles;
	 
	 String code;
	 String prCode;
	 String rolesIdx;
	 String name;

	 Map<String, Object> authData;
}
