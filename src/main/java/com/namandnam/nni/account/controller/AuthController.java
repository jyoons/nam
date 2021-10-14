package com.namandnam.nni.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.namandnam.nni.account.service.AccountService;
import com.namandnam.nni.common.service.CommonService;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.vo.Roles;
import com.namandnam.nni.common.web.CommonController;
import com.namandnam.nni.common.web.Const;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 권한관리 컨트롤러.
 * @author dhkim
 *
 */
@Slf4j
@RequestMapping("/account")
@Controller
@RequiredArgsConstructor
public class AuthController extends CommonController {
	
	private final AccountService accountService;
	
	private final CommonService commonService;
	
	
	/**
	 * index page
	 * @return
	 */
	@GetMapping(value = { "/auth/setting" })
	public String accountMain(ModelMap model, Roles params, HttpServletRequest req) {
		
		
		model.addAttribute("roleList", commonService.selectRolesList() );
		model.addAttribute(Const.KEY_PARAMS, params);
		
		return viewPath("/account/auth/setting", req);
    }
	
	
	/**
	 * 기본 메뉴정보 Loop 편하게 돌리기 위해서
	 * @return
	 */
	@PostMapping("/auth/menu-data")
	@ResponseBody
	public Result MenuListData(ModelMap model, Roles params, HttpServletRequest req) {
		
		return accountService.selectAuthMenuList(null);
	}
	
	
	/**
	 * 메뉴 권한정보 List
	 * @return
	 */
	@PostMapping("/auth/settingData-list")
	public String authMenuListData(ModelMap model, Roles params, HttpServletRequest req) {
		
		Result result = accountService.selectAuthMenuList(params.getRolesIdx());
		model.addAttribute(Const.KEY_PARAMS, params);
		model.addAttribute(Const.KEY_RESULT, result);

		return viewPath("/account/auth/setting_list", req);
	}
	
	
	
	/**
	 * 권한 등록
	 * @return
	 */
	@PostMapping("/auth/setting-data.act")
	@ResponseBody
	public Result addAction( Roles roles , HttpServletRequest req) {

		log.debug(roles.toString());
		
		return accountService.settingAuthGroupMenu(roles);
		
	}
	
	
	
	/**
	 * 권한 그룹 리스트
	 * @return
	 */
	@PostMapping("/auth/authGroupList.act")
	@ResponseBody
	public Result authGroupList( Roles roles , HttpServletRequest req) {
		
		Map<String, Object> data = new HashMap<String, Object>(); 
	    data.put( Const.KEY_ITEM, commonService.selectRolesList());
		
		return Result.builder().success(true).data(data).build();
		
	}
	
	/**
	 * 권한 그룹생성
	 * @return
	 */
	@PostMapping("/auth/generateAuthGroup.act")
	@ResponseBody
	public Result generateAuthGroup( Roles roles , HttpServletRequest req) {

		return accountService.generateAuthGroup(roles);
		
	}
	
	
//	/**
//	 * 권한 수정
//	 * @return
//	 */
//	@AuthCheck(code="CM08D02C01", level = 1)
//	@PostMapping("/auth/upd.act")
//	@ResponseBody
//	public Result updAction(ModelMap model, MngUser mUser, HttpServletRequest req) {
//
//		return accountService.updateMngUser(mUser);
//	}
	
	
}
