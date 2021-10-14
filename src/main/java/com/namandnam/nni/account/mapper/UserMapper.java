package com.namandnam.nni.account.mapper;

import java.util.List;

import com.namandnam.nni.community.vo.Community;
import org.apache.ibatis.annotations.Mapper;

import com.namandnam.nni.account.vo.MngUser;
import com.namandnam.nni.common.vo.MenuAuth;

@Mapper
public interface UserMapper {

	MngUser getUserPassWd(MngUser user);

    List<MenuAuth> selectUserMenuAuth(MngUser user);
    
    int updateUserPassword(MngUser user);
    
    
    
    List<Community> selectMyCommList(Community community);
	
	int selectMyCommListCnt(Community community);
	
//	List<Qna> selectMyQnaList(Qna qna);
//	
//	int selectMyQnaListCnt(Qna qna);

}
