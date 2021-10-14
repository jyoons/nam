package com.namandnam.nni.common.vo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Params extends PageableParams {

//	private String search_showyn;
//	private String search_word;
//	private String btype;
//	private String tblName;
//	private String idx;
//	private String title;
//	private String division;
//	
//	private List<String>  lastYear;
//	private List<Integer>  delChecks;
//	
//	private String strKey; // 검색 키
//	private String strType; // 검색 타입
//	
//	private List<String>  status;
//	private List<String>  areacode;
//	private List<String>  area;
//	private List<String>  householdscode;

	
	private String strVal; // 검색 값
	private String strMsg; // 검색 값
	private String strUserID;
	private String strUserName;
	private String strUserNo;
	private String strUserPW;
	private String strEmil;
	private String strNo;
	private String strCode;
	private String articleNo;
	private String strBoardID;
	private String fileMode;
	
	private String aptCode;
	private String aptSeq;
	private String aptDiv;
	private String aptHo;
	private String mailPath;
	private String deptCd;
	
	private String domain;
	
	private Date regDate;
	private Date dtDate;
}
