package com.namandnam.nni.account.service;

import java.util.List;

import com.namandnam.nni.account.mapper.UserMapper;
import com.namandnam.nni.account.vo.MngUser;
import org.springframework.stereotype.Service;

import com.namandnam.nni.common.utils.Encrypt;
import com.namandnam.nni.common.vo.MenuAuth;
import com.namandnam.nni.common.vo.Result;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    
    /**
     * 유저 passWord Check
     * @param board
     */
    public MngUser checkUserPwd(MngUser mngUser) {
    	
    	MngUser dbUserInfo = userMapper.getUserPassWd(mngUser);
    	
    	if( Encrypt.sha256Hex( mngUser.getPswrd() ).equals( dbUserInfo.getPswrd() ) ) {
//    		mngUser.setIdx( dbUserInfo.getIdx() );
//    		mngUser.setRolesIdx( dbUserInfo.getRolesIdx() );
//    		mngUser.setRolesIdx( dbUserInfo.getRolesIdx() );
//    		mngUser.setRolesIdx( dbUserInfo.getRolesIdx() );
    		return dbUserInfo;
    	}else {
    		
    		//TODO TB_MNGR table에 FAIL_CNT ++
    		return null;
    	}
    	
    }
    
    
    
    /**
     * 유저 메뉴별 권한 리스트
     * @param board
     */
    public List<MenuAuth> selectUserMenuAuth(MngUser user) {
        return userMapper.selectUserMenuAuth( user);
    }
    
    
    
    
    /**
     * 관리자 기존 패스워드 체크
     * @param board
     */
    public Boolean checkAsisPassword(MngUser mngUser) {
    	
    	MngUser dbUserInfo = userMapper.getUserPassWd(mngUser);
    	
    	if( dbUserInfo != null && Encrypt.sha256Hex( mngUser.getAsisPswrd() ).equals( dbUserInfo.getPswrd() ) ) {
    		return true;
    	}
    	
        return false;
    }
    
    
    /**
     * 관리자 비밀번호 변경 Update
     * @param mUser
     */
    public Result updateUserPassword(MngUser mUser) {
	    
    	int rtn = userMapper.updateUserPassword(mUser);

	    return Result.builder().success(  rtn == 1 ? true : false ).build();
    	
    }
    
    
    
//    public Result selectList(Community community, Qna qna) {
//		int totalRecordCount = 0;
//
//		community.setPageSize(7);
//	    totalRecordCount = (Integer) userMapper.selectMyCommListCnt(community);
//	    List<Community> commlist = userMapper.selectMyCommList(community);
//	    
//	    community.setTotalRecordCount(totalRecordCount);
//	    community.paginate();
//
//	    Map<String, Object> data = new HashMap<String, Object>(); 
//	    data.put( "comm", community);
//	    
//	    qna.setPageSize(7);
//	    totalRecordCount = (Integer) userMapper.selectMyQnaListCnt(qna);
//	    List<Qna> qnalist = userMapper.selectMyQnaList(qna);
//	    
//	    qna.setTotalRecordCount(totalRecordCount);
//	    qna.paginate();
// 
//	    data.put( Const.KEY_ITEMS, qnalist);
//	    data.put( "qna", qna);
//	    
//	    return Result.builder().data(data).success(true).build();
//	}

}
