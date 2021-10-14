package com.namandnam.nni.common.web;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;

import ch.qos.logback.classic.Logger;

public class CommonController {

	@Autowired
	protected MessageSource messageSource;
	
	@Value("${spring.profiles.active}")
	private String profiles;
	
	@Value("${env.file.path}")
	private String filePath;
	
    @Autowired
	ServletContext servletContext;

    @Resource
	private MessageSourceAccessor messageSourceAccessor;

	protected String getMessage(String code) {
		return messageSourceAccessor.getMessage(code);
	}

	private static final Logger log = (Logger) LoggerFactory.getLogger(CommonController.class);

	protected String viewPath(String viewPath, HttpServletRequest req) {
		Device device = DeviceUtils.getCurrentDevice(req);
		String strPath = "fo";
		if (!device.isNormal()) {
			strPath = "fo";
		}
		return strPath + viewPath;
	}

	protected String  getEmailPath() {
		String path = servletContext.getRealPath("/WEB-INF/templates/mail/");
		return path;
	}
	
	
	protected String goLogin(HttpServletRequest req) {
		String rtnUrl = req.getRequestURI();
		return "redirect:/user/login?rtnUrl="+rtnUrl;
	}


}
