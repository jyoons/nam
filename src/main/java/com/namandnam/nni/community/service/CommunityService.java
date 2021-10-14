package com.namandnam.nni.community.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.namandnam.nni.community.mapper.CommunityMapper;
import com.namandnam.nni.community.vo.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.constant.TableCategoryEnum;
import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.vo.AttachFile;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.Const;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {
	
	@Autowired
	private UserSessionScopeBean sessionBean;
	
	private final CommunityMapper communityMapper;
	private final CommonMapper commonMapper;
	private final FileService fileService;
	
	
	public int insertNewPost(Community community) {
		community.setUIdx(sessionBean.getUserIdx());
		
		return communityMapper.insertNewPost(community);
	}
	
	
	public int updatePost(Community community) {
		return communityMapper.updatePost(community);
	}
	
	
	public int insertReply(Community community) {
		return communityMapper.insertReply(community);
	}
	
	
	public Community selectOne(String idx) {
		Community community = communityMapper.selectOne(idx);
		
//		if ( !community.getUIdx().equals(sessionBean.getUserIdx()) )
//			community.setUIdx("");
		
		return community;
	}
		
	
	public int updateHitCnt(String idx) {
		return communityMapper.updateHitCnt(idx);
	}
	
	
	public int deletePost(List<String> idxList) {
		return communityMapper.deletePost(idxList);
	}

	

	public Result selectList(Community community) {
		int totalRecordCount = 0;

	    totalRecordCount = (Integer) communityMapper.selectListCnt(community);
	    List<Community> list = communityMapper.selectList(community);
	    
	    community.setTotalRecordCount(totalRecordCount);
	    community.paginate();

	    Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEM, list);
	    data.put( "count", totalRecordCount);
	    data.put( Const.KEY_PARAMS, community);
	    
	    return Result.builder().data(data).success(true).build();
	}
	
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void insertIntoNewPostDataAndFile(Community community, MultipartFile upload) throws Exception{
    	
		communityMapper.insertNewPost(community);
		AttachFile attachFile = fileService.saveFileInfo(upload);
		attachFile.setTblCtgry(TableCategoryEnum.BOARD.getCode());
		attachFile.setTblIdx( community.getIdx() );
		
		commonMapper.insertIntoAttachFile( attachFile );
    }

	
	
	@Transactional
    public void updatePostDataAndFile(Community community, MultipartFile upload) throws Exception{
		communityMapper.updatePost(community);
    	
		AttachFile attachFile = fileService.saveFileInfo(upload);
		log.debug(attachFile.toString());
		attachFile.setTblCtgry( TableCategoryEnum.BOARD.getCode() );
		attachFile.setTblIdx( community.getIdx() );
		
		if (communityMapper.selectOne(community.getIdx()).getFileIdx() != null) {
			commonMapper.updateIntoAttachFile( attachFile );
		} else {
			commonMapper.insertIntoAttachFile(attachFile);
		}
    	
    }
	
	
	
	@Transactional
    public Boolean insertIntoReplyDataAndFile(Community community, MultipartFile upload) {
		communityMapper.insertReply(community);
    	
    	try {
			AttachFile attachFile = fileService.saveFileInfo(upload);
			attachFile.setTblCtgry(TableCategoryEnum.BOARD.getCode());
			attachFile.setTblIdx( community.getIdx() );
			
			commonMapper.insertIntoAttachFile( attachFile );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
    	return true;
    }
}
