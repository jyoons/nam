package com.namandnam.nni.manage.library.vo;

import com.namandnam.nni.common.vo.PageableParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

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
