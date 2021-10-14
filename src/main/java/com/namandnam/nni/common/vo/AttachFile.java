package com.namandnam.nni.common.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("AttachFile")
public class AttachFile {
    
	 String idx;
	 String tblIdx;
	 String tblCtgry;
	 String fileType;
	 String fileName;
	 String orgnlFileName;
	 String filePath;
	 String fileSize;
	 String fileSort;
	 String fileDesc;
	 String regDt;
	 
	 String subPath;
	
}
