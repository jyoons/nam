package com.namandnam.nni.manage.library.controller;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.vo.Params;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.manage.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 파 일 명 : LibraryController
 * 작 성 자 : smlee
 * 날    짜 : 2021-10-14 오전 11:17
 * 설    명 : 메인 > 소식 & 자료 > 자료실 Controller
 */
@Slf4j
@Controller
@RequestMapping("/manage/library")
@RequiredArgsConstructor
public class LibraryController {

    @Autowired
    private UserSessionScopeBean userSessionBean;

    private final LibraryService libraryService;
    private final FileService fileService;


    @GetMapping("/list")
    public String main(ModelMap model, Params param, HttpServletRequest req) {

        log.debug(param.toString());

        model.addAttribute(Const.KEY_PARAMS, param);

        return "manage/library/list";
    }

}
