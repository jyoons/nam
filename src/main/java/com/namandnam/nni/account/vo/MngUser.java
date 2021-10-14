package com.namandnam.nni.account.vo;

import org.apache.ibatis.type.Alias;

import com.namandnam.nni.common.vo.PageableParams;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Alias("MngUser")
@EqualsAndHashCode(callSuper=false)
public class MngUser extends PageableParams {	
	
	String idx;
	String rolesIdx;
	String mngrId;
	String pswrd;
	String name;
	String isLock;
	String cmnt;
	String failCnt;
	String dtLogin;
	String isDlt;
	String dtDdfd;
	String reg;
	
	String roles;
	
	String strKey;
	String srchTxt;
	
	
	String asisPswrd;
	String tobePswrd;
	String cirmPswrd;
	
}
