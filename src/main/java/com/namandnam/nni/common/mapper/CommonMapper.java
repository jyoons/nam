package com.namandnam.nni.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.namandnam.nni.common.vo.AttachFile;
import com.namandnam.nni.common.vo.Roles;
import com.namandnam.nni.common.vo.SampleVo;

@Mapper
public interface CommonMapper {
	
	List<Roles> selectRolesList();

	AttachFile selectOneFileInfo(String fileIdx);
	
	AttachFile selectOneByIdx(String fileIdx);
	
	int insertIntoAttachFile( AttachFile af );
	
	int updateIntoAttachFile( AttachFile af );
	
	int deleteAttachFile( String fileIdx );
	
	
	/**
	 * 아래부터는 sample 관련 method
	 */

	SampleVo selectOneSampleData( String idx );

	int insertIntoSampleData( SampleVo sv );
	
	List<Map<String, Object>> selectSampleList( List<String> af );

}
