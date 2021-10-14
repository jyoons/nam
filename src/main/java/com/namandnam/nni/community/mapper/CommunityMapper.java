package com.namandnam.nni.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namandnam.nni.community.vo.Community;

@Mapper
public interface CommunityMapper {
	List<Community> selectList(Community community);
	
	Community selectOne(String idx);
	
	int selectListCnt(Community community);
	
	int insertNewPost(Community community);
	
	int insertReply(Community community);
	
	int updateHitCnt(String idx);
	
	int updatePost(Community community);
	
	int deletePost(List<String> idxList);	
}
