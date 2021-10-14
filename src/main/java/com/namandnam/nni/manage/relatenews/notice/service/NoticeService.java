package com.namandnam.nni.manage.relatenews.notice.service;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.constant.TableCategoryEnum;
import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.vo.AttachFile;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.manage.relatenews.notice.mapper.NoticeMapper;
import com.namandnam.nni.manage.relatenews.notice.vo.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
	
	@Autowired
	private UserSessionScopeBean sessionBean;
	
	private final NoticeMapper noticeMapper;
	private final CommonMapper commonMapper;
	private final FileService fileService;
	
	
	public int insertNewPost(Notice notice) {
		notice.setUIdx(sessionBean.getUserIdx());
		
		return noticeMapper.insertNewPost(notice);
	}
	
	
	public int updatePost(Notice notice) {
		return noticeMapper.updatePost(notice);
	}
	
	
	public int insertReply(Notice notice) {
		return noticeMapper.insertReply(notice);
	}
	
	
	public Notice selectOne(String idx) {
		Notice notice = noticeMapper.selectOne(idx);
		
//		if ( !community.getUIdx().equals(sessionBean.getUserIdx()) )
//			community.setUIdx("");
		
		return notice;
	}
		
	
	public int updateHitCnt(String idx) {
		return noticeMapper.updateHitCnt(idx);
	}
	
	
	public int deletePost(List<String> idxList) {
		return noticeMapper.deletePost(idxList);
	}

	

	public Result selectList(Notice notice) {
		int totalRecordCount = 0;

	    totalRecordCount = (Integer) noticeMapper.selectListCnt(notice);
	    List<Notice> list = noticeMapper.selectList(notice);

		notice.setTotalRecordCount(totalRecordCount);
		notice.paginate();

	    Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEM, list);
	    data.put( "count", totalRecordCount);
	    data.put( Const.KEY_PARAMS, notice);
	    
	    return Result.builder().data(data).success(true).build();
	}
	
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void insertIntoNewPostDataAndFile(Notice notice, MultipartFile upload) throws Exception{

		noticeMapper.insertNewPost(notice);
		AttachFile attachFile = fileService.saveFileInfo(upload);
		attachFile.setTblCtgry(TableCategoryEnum.BOARD.getCode());
		attachFile.setTblIdx( notice.getIdx() );
		
		commonMapper.insertIntoAttachFile( attachFile );
    }

	
	
	@Transactional
    public void updatePostDataAndFile(Notice notice, MultipartFile upload) throws Exception{
		noticeMapper.updatePost(notice);
    	
		AttachFile attachFile = fileService.saveFileInfo(upload);
		log.debug(attachFile.toString());
		attachFile.setTblCtgry( TableCategoryEnum.BOARD.getCode() );
		attachFile.setTblIdx( notice.getIdx() );
		
		if (noticeMapper.selectOne(notice.getIdx()).getFileIdx() != null) {
			commonMapper.updateIntoAttachFile( attachFile );
		} else {
			commonMapper.insertIntoAttachFile(attachFile);
		}
    	
    }
	
	
	
	@Transactional
    public Boolean insertIntoReplyDataAndFile(Notice notice, MultipartFile upload) {
		noticeMapper.insertReply(notice);
    	
    	try {
			AttachFile attachFile = fileService.saveFileInfo(upload);
			attachFile.setTblCtgry(TableCategoryEnum.BOARD.getCode());
			attachFile.setTblIdx( notice.getIdx() );
			
			commonMapper.insertIntoAttachFile( attachFile );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
    	return true;
    }
}
