package com.namandnam.nni.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ModelAttributeController {
	
//	@Autowired
//	private MainBoardService mainBoardService;
	
	@Autowired
	private UserSessionScopeBean userSession;
	
	@ModelAttribute
	public void addAttributes(HttpServletRequest req, Model model) {
		addCommonAttributes(req, model);
	}
	
	public void addCommonAttributes(HttpServletRequest req, Model model) {
		String uri = req.getRequestURI();
		String queryString = req.getQueryString() == null ? "" : "?" + req.getQueryString();
		
		
//		if(CommonSession.me() != null) {
//			Result result = mainBoardService.selectList();
//			model.addAttribute(Const.KEY_RESULT, result);
//		}else {
//			model.addAttribute(Const.KEY_RESULT, null);
//		}
		
		
//		model.addAttribute("me", CommonSession.me());
		model.addAttribute("uri", uri + queryString);
		model.addAttribute("userName", userSession.getName() );
		
	}
	
	
	@ExceptionHandler(HttpStatusCodeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView  noAuthProccess(HttpServletRequest request, HttpServletResponse response, HttpStatusCodeException e){
        
        //makeExceptionAttribute(request, HttpStatus.INTERNAL_SERVER_ERROR, e, null);
 
        return new ModelAndView("/error/404");
    }


}
