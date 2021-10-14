package com.namandnam.nni.config;

import javax.servlet.http.HttpServletRequest;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ReflexionAspect {

    @Autowired
    private UserSessionScopeBean sessionBean;

    /**
     * cache 장바구니 session에 담기.
     * 
     * @param jpoint
     * @throws Throwable
     */
    
//  @Around("execution(* kr.co.ref.**.controller.*.*(..)) && !@annotation(IgnoreCheckSession)")
    @Around("( execution(* com.namandnam.nni.*.*.*.controller.*.*(..)) || execution(* com.namandnam.nni.*.*.controller.*.*(..)) || execution(* com.namandnam.nni.*.controller.*.*(..))) "
    		+ " && !@annotation(com.namandnam.nni.config.IgnoreCheckSession)")
    public Object beforeChecksession(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        
        log.debug( "before : {}" ,  request.getRequestURI() );
        
//        if( sessionBean == null || sessionBean.getUserIdx() == null || sessionBean.getUserIdx().trim().equals("") ){
//        	return "redirect:/user/login";
//        }
        
        return  joinPoint.proceed();
    }
    
	@SuppressWarnings("serial")
	@Before("@annotation(com.namandnam.nni.config.AuthCheck) && @ annotation(authcheck)")
    public void beforeCheckAuth(JoinPoint joinPoint, AuthCheck authcheck) throws Throwable {
    	
    	// HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    	
    	log.debug( authcheck.code() );
    	log.debug( authcheck.level() + "" );
    	
    	if( sessionBean != null && sessionBean.getAuth().get( authcheck.code() ) < authcheck.level() ) {
    		throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_AUTH") {};
    		
//    		PrintWriter writer = response.getWriter();
//			writer.print("<script>");
//			writer.print("alert('No authority');");
//			writer.print("location.href='/';");
//			writer.print("</script>");
//			writer.close();
    		
    	}
    	
    	
    }

}
