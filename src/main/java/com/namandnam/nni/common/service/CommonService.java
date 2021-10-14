/*
 * Copyright yysvip.tistory.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of yysvip.tistory.com.,LTD. ("Confidential Information").
 */
package com.namandnam.nni.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.vo.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommonService {
	
	private final CommonMapper commonMapper;
	
	/**
	 * 권한 리스트 
	 * @return
	 * @throws Exception
	 */
	public List<Roles> selectRolesList() {
		
		log.debug("Roles List");
		
		return commonMapper.selectRolesList();
	}

}
