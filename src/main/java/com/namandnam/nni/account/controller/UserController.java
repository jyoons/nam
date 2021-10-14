package com.namandnam.nni.account.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.account.vo.MngUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.namandnam.nni.account.service.UserService;
import com.namandnam.nni.common.utils.Encrypt;
import com.namandnam.nni.common.vo.MenuAuth;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.CommonController;
import com.namandnam.nni.config.IgnoreCheckSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저 페이지.
 * @author dhkim
 *
 */
@Slf4j
@RequestMapping("/user")
@Controller
public class UserController extends CommonController {
	
	 @Autowired
	 private UserSessionScopeBean sessionBean;
	 
	 @Autowired
	 private UserService userService;
	
	/**
	 * 로그인 페이지 이동
	 * @return
	 */
	@IgnoreCheckSession
	@GetMapping(value = { "/login" })
	public String main(ModelMap model, HttpServletRequest req) {
		
		if( sessionBean.getAuth() != null )
			return "redirect:/dshbr/main";
		
		return viewPath("/user/login_page", req);
    }
	
	
	@IgnoreCheckSession
	@ResponseBody
	@PostMapping(value = { "/login.act" })
	public Result loginAct(MngUser mngUser, HttpServletRequest req) {
		
		log.debug(">>> LOGIN PROCESS ");
		//TODO 추후 로그인 프로세스
		
		MngUser dbInfoUser = userService.checkUserPwd(mngUser);
		
		if( dbInfoUser != null ) {
			
			sessionBean.setUserIdx(dbInfoUser.getIdx());
			sessionBean.setName(dbInfoUser.getName());
			sessionBean.setMngrId(dbInfoUser.getMngrId());
			sessionBean.setFailCnt(dbInfoUser.getFailCnt());
			
			mngUser.setRolesIdx( dbInfoUser.getRolesIdx() );
			
			//메뉴 권한 리스트
			List<MenuAuth> menuAuthList= userService.selectUserMenuAuth(mngUser);
			
			setMenuAuth( menuAuthList );
			
			
		}else {
			return Result.builder().success(false).message("로그인정보를 정확히 입력하세요").build();
		}
		
		return Result.builder().success(true).build();
    }
	
	
	@IgnoreCheckSession
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "redirect:/user/login";
	}
	
	
	/**
	 * myPage 페이지 이동
	 * @return
	 */
	@GetMapping(value = { "/mypage" })
	public String mypage(ModelMap model, HttpServletRequest req) {
		
		
		return viewPath("/user/mypage_index", req);
    }
	
	
	private void setMenuAuth( List<MenuAuth> mList) {
		
		Map<String, Integer> auth = new HashMap<String, Integer>();
		
		for( MenuAuth ma : mList ) {
			auth.put( ma.getCode(), ma.getAuth() );
		}
		
		sessionBean.setAuth(auth);
		
	}
	
	
	
	
	/**
	 * 비밀번호 변경
	 * @return
	 */
	@PostMapping("/updPasswd.act")
	@ResponseBody
	public Result updPasswd(ModelMap model, MngUser mngUser, HttpServletRequest req) {
		
		mngUser.setIdx( sessionBean.getUserIdx() );
		
		if( !userService.checkAsisPassword(mngUser) ) {
			return Result.builder()
						.success(false )
						.message("기존 패스워드가 정확하지 않습니다.")
						.build();
		}
		
		if( mngUser.getTobePswrd().equals( mngUser.getAsisPswrd() ) ) {
			return Result.builder()
					.success(false )
					.message("기존 패스워드와 신규 패스워드가 동일합니다.")
					.build();
		}
		
		
		if( !mngUser.getTobePswrd().equals( mngUser.getCirmPswrd() ) ) {
			return Result.builder()
					.success(false )
					.message("신규 패스워드 확인을 해주세요.")
					.build();
		}
		
		mngUser.setPswrd( Encrypt.sha256Hex( mngUser.getTobePswrd() ) );
		
		return userService.updateUserPassword(mngUser);
		
	}
	
//	@PostMapping("/list-data")
//	public String communityListData(ModelMap model, Community community, Qna qna, HttpServletRequest req) {
//		
//		community.setUIdx(  sessionBean.getUserIdx() );
//		qna.setUIdx(  sessionBean.getUserIdx() );
//		
//		Result result = userService.selectList(community, qna);
//		
//		model.addAttribute(Const.KEY_RESULT, result);
//
//		return viewPath("/dashboard/dshbr_list", req);
//	}
	
	
}
