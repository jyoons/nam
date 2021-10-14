package com.namandnam.nni.account.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.namandnam.nni.account.mapper.AccountMapper;
import com.namandnam.nni.account.vo.MngUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namandnam.nni.common.utils.Encrypt;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.vo.Roles;
import com.namandnam.nni.common.web.Const;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    /**
     * 관리자 리스트
     * @param mUser
     */
    public Result selectMngUserList(MngUser mUser) {
    	
    	
    	int totalRecordCount = 0;
    	mUser.setRecordCountPerPage(20);
	    totalRecordCount = (Integer) accountMapper.selectMngUserListCnt(mUser);
	    
	    List<MngUser> list = (List<MngUser>) accountMapper.selectMngUserList(mUser);
	    
	    mUser.setTotalRecordCount(totalRecordCount);
	    mUser.paginate();

	    Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEMS, list);
	    data.put( "count", totalRecordCount);
	    data.put( Const.KEY_PARAMS, mUser);
	    
	    return Result.builder().data(data).success(true).build();
    	
    }
    
    
    /**
     * 관리자 선택 One
     * @param mUser
     */
    public Result selectMngOne(String idx) {
	    
    	MngUser mnguser = accountMapper.selectOneMngUser(idx);

	    Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEM, mnguser);
	    
	    return Result.builder().data(data).success(true).build();
    	
    }
    
    
    /**
     * 관리자 Insert
     * @param mUser
     */
    public Result insertMngUser(MngUser mUser) {
    	
    	mUser.setPswrd( Encrypt.sha256Hex( mUser.getPswrd() ) );
	    
    	int rtn = accountMapper.insertMngUser(mUser);
	    
	    return Result.builder().success( rtn == 1 ? true : false ).build();
    	
    }
    
    /**
     * 관리자 Update
     * @param mUser
     */
    public Result updateMngUser(MngUser mUser) {
	    
    	int rtn = accountMapper.updateMngUser(mUser);

	    return Result.builder().success(  rtn == 1 ? true : false ).build();
    	
    }
    
    
    
    
    
    
    
    
    
    /**
     * 권한별 메뉴 리스트
     * @param mUser
     */
    public Result selectAuthMenuList(String roloIdx) {
	    
    	List<Roles> list = accountMapper.selectAuthMenuList(roloIdx);
	    
	    Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEMS, list);
	    
	    return Result.builder().data(data).success(true).build();
    	
    }
    
    
    /**
     * 권한그룹 별 메뉴 권한 설정.
     * @param mUser
     */
    @Transactional
    public Result settingAuthGroupMenu( Roles roles ) {
	    
    	//delInt
    	int delInt = accountMapper.deleteRolesMenu( roles.getRolesIdx() );
	    
    	log.debug( " > " + delInt );
    	
    	int insInt = accountMapper.insertRolesMenu( roles );
    	
    	log.debug( " > " + insInt );
	    
	    return Result.builder().success( insInt > 0 ? true : false).build();
    	
    }
    
    
    /**
     * 권한그룹 별 메뉴 권한 설정.
     * @param mUser
     */
    @Transactional
    public Result generateAuthGroup( Roles roles ) {
	    
    	//delInt
    	int checkCnt = accountMapper.selectDuplicationName( roles );
	    
    	if( checkCnt > 0 ) {
    		return  Result.builder().success(false).message("동일한 이름의 권한그룹이 존재합니다. ").build();
    	}
    	
    	int insInt = accountMapper.insertRoles( roles );
    	Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEM, roles);
	    
	    return Result.builder().data(data) .success( insInt > 0 ? true : false).build();
    	
    }
    

}
