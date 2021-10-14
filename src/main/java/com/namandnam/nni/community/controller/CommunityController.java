package com.namandnam.nni.community.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.constant.BoardTypeEnum;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.utils.CommUtil;
import com.namandnam.nni.common.vo.AttachFile;
import com.namandnam.nni.common.vo.Params;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.CommonController;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.community.service.CommunityService;
import com.namandnam.nni.community.vo.Community;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController extends CommonController {

	
	@Autowired
	private UserSessionScopeBean userSessionBean;
	
	private final CommunityService communityService;
	private final FileService fileService;
	
	
	@GetMapping("/board")
	public String main(ModelMap model, Params param, HttpServletRequest req) {
		
		log.debug(param.toString());
		
		model.addAttribute(Const.KEY_PARAMS, param);
		
		return viewPath("/community/board_index", req);
	}
	
	@PostMapping("/list-data")
	public String communityListData(ModelMap model, Community community, HttpServletRequest req) {
		
		community.setBoardCtgy( BoardTypeEnum.NOTICE.getCode() );
		Result result = communityService.selectList(community);
		
		model.addAttribute(Const.KEY_RESULT, result);

		return viewPath("/community/board_list", req);
	}
	
	@GetMapping("/post")
	public String viewPost(@RequestParam("idx") String idx, ModelMap model, HttpServletRequest req) {
		Community community = communityService.selectOne(idx);
		communityService.updateHitCnt(idx);
		
		model.addAttribute(Const.KEY_PARAMS, community);
		
		return viewPath("/community/board_view", req);
	}
	
	
	@GetMapping("/regPost")
	public String regPost(ModelMap model, Community community, HttpServletRequest req) {
		return viewPath("/community/board_reg", req);
	}
	
	@PostMapping("/regPost")
	public String regReply(ModelMap model, Community community, HttpServletRequest req) {
//		model.addAttribute(Const.KEY_ITEM, community.getReplyIdx());

		return viewPath("/community/board_reg", req);
	}
	
	@PostMapping("/editPost")
	public String editPost(ModelMap model, Community community, HttpServletRequest req) {
		model.addAttribute(Const.KEY_RESULT, communityService.selectOne(community.getIdx()));

		return viewPath("/community/board_reg", req);
	}
	
	
	
	@PostMapping("/add.act")
	@ResponseBody
	public Result addAct(ModelMap model, Community community, HttpServletRequest req, MultipartHttpServletRequest mreq) {
		
		Result result = checkValid(community);
		
		if (result.isSuccess() && BoardTypeEnum.isValid( community.getBoardType() ) ) {
			
			//session 처리시 변경 해야함.
			community.setRegId( userSessionBean.getUserIdx() == null ? "system" : userSessionBean.getUserIdx() );
			
			if (mreq != null) {
				
				MultipartFile mfile = mreq.getFile("file");
				
				if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
					if ( checkFileExtension(mfile) ) {
						
						try {
							communityService.insertIntoNewPostDataAndFile(community, mfile);
						}catch (Exception e) {
							result.setSuccess(false);
							result.setMessage("실패 하였습니다. 잠시 후 다시 시도해 주세요.");
						}
						
					} else {
						result.setSuccess(false);
						result.setMessage("지원하는 파일 확장자가 아닙니다.");
					}
					
				} else if (communityService.insertNewPost(community) > 0 ) {
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
		
		if (communityService.deletePost(idxList) > 0) {
			result.setSuccess(true);
			result.setMessage("삭제 되었습니다.");
		}
		
		return result;
	}
	
	@PostMapping("/upd.act")
	@ResponseBody
	public Result updAct(ModelMap model, Community community, HttpServletRequest req, MultipartHttpServletRequest mreq) {
		Result result = checkValid(community);
		if (result.isSuccess()) {
			community.setBoardType( BoardTypeEnum.NOTICE.getCode() );
			community.setUIdx( userSessionBean.getUserIdx() );
			
			if (mreq != null) {
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
					if ( checkFileExtension(mfile) ) {
						
						try {
							communityService.updatePostDataAndFile(community, mfile);
						}catch (Exception e) {
							result.setSuccess(false);
							result.setMessage("수정에 실패 하였습니다. 잠시 후 다시 시도해 주세요.");
						}
						
					} else {
						result.setSuccess(false);
						result.setMessage("지원하는 파일 확장자가 아닙니다.");
					}
				} else if (communityService.updatePost(community) > 0 ) {
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
	public Result addReplyAct(ModelMap model, Community community, HttpServletRequest req, MultipartHttpServletRequest mreq) {
		Result result = checkValid(community);
		if (result.isSuccess()) {
			
			community.setUIdx( userSessionBean.getUserIdx() );
			
			if (mreq != null) {
				MultipartFile mfile = mreq.getFile("file");
				if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
					if ( checkFileExtension(mfile) ) {
						if( communityService.insertIntoReplyDataAndFile(community, mfile) ) {
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
				} else if (communityService.insertReply(community) > 0 ) {
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
    public HashMap<String, Object> communityImageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {
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
	
	private Result checkValid(Community community) {
		boolean isSuccess = true;
		String message = "";
		
		if (community.getTitle().length() > 200) {
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
