package com.namandnam.nni.common.vo;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Alias("SampleVo")
public class SampleVo {
    
	public SampleVo() {
	}
	
	public SampleVo(String idx, String title, String contents) {
		this.idx = idx;
		this.title = title;
		this.contents = contents;
	}
	String idx;
	 String title;
	 String contents;
	
}
