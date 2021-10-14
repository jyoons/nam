package com.namandnam.nni.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.namandnam.nni.account.vo.MngUser;
import com.namandnam.nni.common.vo.Roles;


@Mapper
public interface AccountMapper {

	//관리자 카운트
	int selectMngUserListCnt(MngUser user);
	
	//관리자 리스트
	List<MngUser> selectMngUserList(MngUser user);
	
	//관리자 정도
	MngUser selectOneMngUser(String idx);
	
	//관리자 추가
	int insertMngUser(MngUser user);
	
	//관리자 수정
	int updateMngUser(MngUser user);
	
	
	
	//권한별 메뉴 리스트
	List<Roles> selectAuthMenuList(String rolesIdx);
	
	//권한그룹 삭제
	@Transactional(propagation = Propagation.REQUIRED)
	int deleteRolesMenu(String rolesIdx);
			
	//권한그룹별 메뉴 권한 추가.
	@Transactional(propagation = Propagation.REQUIRED)
	int insertRolesMenu(Roles roles);
	 
	
	// 중복 권한 이름 체크
	int selectDuplicationName( Roles roles );
	
	// 권한그룹 추가.
	int insertRoles(Roles roles);
		 
	
}
