package com.namandnam.nni.manage.relatenews.notice.mapper;

import com.namandnam.nni.manage.relatenews.notice.vo.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
	List<Notice> selectList(Notice notice);

	Notice selectOne(String idx);
	
	int selectListCnt(Notice notice);
	
	int insertNewPost(Notice notice);
	
	int insertReply(Notice notice);
	
	int updateHitCnt(String idx);
	
	int updatePost(Notice notice);
	
	int deletePost(List<String> idxList);	
}
