package com.namandnam.nni.manage.board.library.vo;

import com.namandnam.nni.common.vo.PageableParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 파 일 명 : Library
 * 작 성 자 : smlee
 * 날    짜 : 2021-10-14 오전 11:23
 * 설    명 : 메인 > 소식 & 자료 > 자료실 VO
 */
@Data
@Alias("Library")
@EqualsAndHashCode(callSuper=false)
public class Library extends PageableParams {
	private String idx;
	private String boardType;
	private String boardTypeNm;
	private String boardCtgy;
	private String title;
	private String contents;
	private String viewCnt;
	private String delYn;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;
	
	
	private String fileName;
	private String fileIdx;
	
	private String uIdx;
}
