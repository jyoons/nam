package com.namandnam.nni.manage.relatenews.notice.controller;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.constant.BoardTypeEnum;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.utils.CommUtil;
import com.namandnam.nni.common.vo.AttachFile;
import com.namandnam.nni.common.web.CommonController;
import com.namandnam.nni.common.vo.Params;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.manage.relatenews.notice.service.NoticeService;
import com.namandnam.nni.manage.relatenews.notice.vo.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/manage/relatenews/notice")
@RequiredArgsConstructor
public class NoticeController extends CommonController {
	
	@Autowired
	private UserSessionScopeBean userSessionBean;
	
	private final NoticeService noticeService;
	private final FileService fileService;
	
	
	@GetMapping("/list")
	public String main(ModelMap model, Params param, HttpServletRequest req) {
		
		log.debug(param.toString());
		
		model.addAttribute(Const.KEY_PARAMS, param);

		return "manage/relatenews/notice/notice_index";
	}
	
	@PostMapping("/list-data")
	public String listData(ModelMap model, Notice notice, HttpServletRequest req) {

		notice.setBoardCtgy( BoardTypeEnum.NOTICE.getCode() );
		Result result = noticeService.selectList(notice);
		
		model.addAttribute(Const.KEY_RESULT, result);

		return "manage/relatenews/notice/notice_list";
		//return viewPath("/community/board_list", req);
	}
	
	@GetMapping("/view")
	public String view(@RequestParam("idx") String idx, ModelMap model, HttpServletRequest req) {
		Notice notice = noticeService.selectOne(idx);
		noticeService.updateHitCnt(idx);
		
		model.addAttribute(Const.KEY_PARAMS, notice);

		return "manage/relatenews/notice/notice_view";
		//return viewPath("/community/board_view", req);
	}
	
	@GetMapping("/regPost")
	public String regPost(ModelMap model, Notice notice, HttpServletRequest req) {
		return "manage/relatenews/notice/notice_reg";
		//return viewPath("/community/board_reg", req);
	}
	
	@PostMapping("/regPost")
	public String regReply(ModelMap model, Notice notice, HttpServletRequest req) {
//		model.addAttribute(Const.KEY_ITEM, community.getReplyIdx());

		return "manage/relatenews/notice/notice_reg";
		//return viewPath("/community/board_reg", req);
	}
	
	@PostMapping("/editPost")
	public String editPost(ModelMap model, Notice notice, HttpServletRequest req) {
		model.addAttribute(Const.KEY_RESULT, noticeService.selectOne(notice.getIdx()));

		return "manage/relatenews/notice/notice_reg";
		//return viewPath("/community/board_reg", req);
	}
	
	@PostMapping("/add.act")
	@ResponseBody
	public Result addAct(ModelMap model, Notice notice, HttpServletRequest req, MultipartHttpServletRequest mreq) {
		
		Result result = checkValid(notice);
		
		if (result.isSuccess() && BoardTypeEnum.isValid( notice.getBoardType() ) ) {
			
			//session 처리시 변경 해야함.
			notice.setRegId( userSessionBean.getUserIdx() == null ? "system" : userSessionBean.getUserIdx() );
			
			if (mreq != null) {
				
				MultipartFile mfile = mreq.getFile("file");
				
				if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
					if ( checkFileExtension(mfile) ) {
						
						try {
							noticeService.insertIntoNewPostDataAndFile(notice, mfile);
						}catch (Exception e) {
							result.setSuccess(false);
							result.setMessage("실패 하였습니다. 잠시 후 다시 시도해 주세요.");
						}
						
					} else {
						result.setSuccess(false);
						result.setMessage("지원하는 파일 확장자가 아닙니다.");
					}
					
				} else if (noticeService.insertNewPost(notice) > 0 ) {
					result.setSuccess(true);
					result.setMessage("저장 하였습니다.");
				} else {
					result.setSuccess(false);
					result.setMessage("실패 하였습니다. 잠시 후 다시 시도해 주세요.");
				}
			}
		}else {
			result.setSuccess(false);
			result.setMessage("실패 하였습니다. 잠시 후 다시 시도해 주세요.");
		}
		
		return result;
	}
	
	@PostMapping("/del.act")
	@ResponseBody
	public Result delAct(@RequestBody List<Map<String, String>> param, ModelMap model, HttpServletRequest req) {
		Result result = Result.builder()
								.success(false)
								.message("게시글 삭제 중 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.")
								.build();
		
		List<String> idxList = new ArrayList<String>();
		for (Map<String, String> item : param)
			idxList.add(item.get("idx"));
		
		if (noticeService.deletePost(idxList) > 0) {
			result.setSuccess(true);
			result.setMessage("삭제 되었습니다.");
		}
		
		return result;
	}
	
	@PostMapping("/upd.act")
	@ResponseBody
	public Result updAct(ModelMap model, Notice notice, HttpServletRequest req, MultipartHttpServletRequest mreq) {
		Result result = checkValid(notice);
		if (result.isSuccess()) {
			notice.setBoardType( BoardTypeEnum.NOTICE.getCode() );
			notice.setUIdx( userSessionBean.getUserIdx() );
			
			if (mreq != null) {
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
					if ( checkFileExtension(mfile) ) {
						
						try {
							noticeService.updatePostDataAndFile(notice, mfile);
						}catch (Exception e) {
							result.setSuccess(false);
							result.setMessage("수정에 실패 하였습니다. 잠시 후 다시 시도해 주세요.");
						}
						
					} else {
						result.setSuccess(false);
						result.setMessage("지원하는 파일 확장자가 아닙니다.");
					}
				} else if (noticeService.updatePost(notice) > 0 ) {
					result.setSuccess(true);
					result.setMessage("수정 하였습니다.");
				} else {
					result.setSuccess(false);
					result.setMessage("수정에 실패 하였습니다. 잠시 후 다시 시도해 주세요.");
				}
			}
		}
		
		return result;
	}
	
	@PostMapping("/addReply.act")
	@ResponseBody
	public Result addReplyAct(ModelMap model, Notice notice, HttpServletRequest req, MultipartHttpServletRequest mreq) {
		Result result = checkValid(notice);
		if (result.isSuccess()) {

			notice.setUIdx( userSessionBean.getUserIdx() );
			
			if (mreq != null) {
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
					if ( checkFileExtension(mfile) ) {
						if( noticeService.insertIntoReplyDataAndFile(notice, mfile) ) {
							result.setSuccess(true);
							result.setMessage("수정 하였습니다.");
						} else {
							result.setSuccess(false);
							result.setMessage("수정에 실패 하였습니다. 잠시 후 다시 시도해 주세요.");
						}
					} else {
						result.setSuccess(false);
						result.setMessage("지원하는 파일 확장자가 아닙니다.");
					}
				} else if (noticeService.insertReply(notice) > 0 ) {
					result.setSuccess(true);
					result.setMessage("수정 하였습니다.");
				} else {
					result.setSuccess(false);
					result.setMessage("수정에 실패 하였습니다. 잠시 후 다시 시도해 주세요.");
				}
			}
		}
		
		
		return result;
	}
	
	@PostMapping("/regPost/editorImageUpload.do")
    @ResponseBody
    public HashMap<String, Object> imageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
    		AttachFile attachFile = fileService.saveFileInfo(upload, "public");
            String fileUrl = "/attach/" + attachFile.getSubPath() + "/" + attachFile.getFileName();

            result.put("filename", attachFile.getFileName());
            result.put("uploaded", 1);
            result.put("url", fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } 

		return result;
    }
	
	private Result checkValid(Notice notice) {
		boolean isSuccess = true;
		String message = "";
		
		if (notice.getTitle().length() > 200) {
			isSuccess = false;
			message = "게시글 제목을 200자 미만으로 설정해주세요.";
		}
		
		return Result.builder()
						.success(isSuccess)
						.message(message)
						.build();
	}
	
	private boolean checkFileExtension(MultipartFile file) {
		String[] validExt = {"gif","png","jpg","jpeg","doc","docx","xls","xlsx","hwp","pdf","ppt","pptx","zip","mp4"};

		String originalFileName = CommUtil.cleanFName(file.getOriginalFilename());
    	String originalFileExtension = (FilenameUtils.getExtension(originalFileName)).toLowerCase();
    	
    	for (int i = 0; i < validExt.length; i++)
    		if (originalFileExtension.toLowerCase().equalsIgnoreCase(validExt[i]))
    			return true;
    	return false;
	}
}
