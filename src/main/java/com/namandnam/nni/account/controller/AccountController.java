package com.namandnam.nni.account.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.namandnam.nni.account.service.AccountService;
import com.namandnam.nni.account.vo.MngUser;
import com.namandnam.nni.common.service.CommonService;
import com.namandnam.nni.common.vo.Params;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.vo.Roles;
import com.namandnam.nni.common.web.CommonController;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.config.AuthCheck;
import lombok.RequiredArgsConstructor;

/**
 * 계정관리 컨트롤러.
 * @author dhkim
 *
 */
@RequestMapping("/account")
@Controller
@RequiredArgsConstructor
public class AccountController extends CommonController {
	
	private final AccountService accountService;
	
	private final CommonService commonService;
	
	
	/**
	 * index page
	 * @return
	 */
	@GetMapping(value = { "/mng/list" })
	public String accountMain(ModelMap model, Params params, HttpServletRequest req) {
		
		model.addAttribute(Const.KEY_PARAMS, params);
		
		return viewPath("/account/mng/account_index", req);
    }
	
	/**
	 * list 관리자 정보 List
	 * @return
	 */
	@PostMapping("/mng/list-data")
	public String accountListData(ModelMap model, MngUser params, HttpServletRequest req) {
		Result result = accountService.selectMngUserList(params);
		model.addAttribute(Const.KEY_PARAMS, params);
		model.addAttribute(Const.KEY_RESULT, result);

		return viewPath("/account/mng/account_list", req);
	}
	
	
	/**
	 * 관리자 추가 페이지 이동
	 * @return
	 */
	@GetMapping("/mng/add")
	public String accountAdd(ModelMap model, MngUser params, HttpServletRequest req) {
		
		List<Roles> roleList = commonService.selectRolesList();
		model.addAttribute(Const.KEY_PARAMS, params);
		model.addAttribute("roleList", roleList);

		return viewPath("/account/mng/account_add", req);
	}
	
	
	/**
	 * 관리자 수정 View 이동
	 * @return
	 */
	@GetMapping("/mng/view")
	public String accountView(ModelMap model, MngUser params, HttpServletRequest req) {
		
		List<Roles> roleList = commonService.selectRolesList();
		
		Result result = accountService.selectMngOne( params.getIdx() );
		model.addAttribute(Const.KEY_PARAMS, params);
		model.addAttribute(Const.KEY_RESULT, result);
		model.addAttribute("roleList", roleList);

		return viewPath("/account/mng/account_add", req);
	}
	
	
	/**
	 * 관리자 등록
	 * @return
	 */
	@AuthCheck(code="CM08D02C01", level = 1)
	@PostMapping("/mng/add.act")
	@ResponseBody
	public Result addAction(ModelMap model, MngUser mUser, HttpServletRequest req) {

		return accountService.insertMngUser(mUser);
		
	}
	
	
	/**
	 * 관리자 수정
	 * @return
	 */
	@AuthCheck(code="CM08D02C01", level = 1)
	@PostMapping("/mng/upd.act")
	@ResponseBody
	public Result updAction(ModelMap model, MngUser mUser, HttpServletRequest req) {

		return accountService.updateMngUser(mUser);
	}
	
	
}
