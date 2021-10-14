package com.namandnam.nni.account.service;

import java.io.Serializable;
import java.util.Map;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@Component
@Data
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionScopeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userIdx;
	private String name;
	private String mngrId;
	private String failCnt;
	
	private Map<String, Integer> auth;
	
}
