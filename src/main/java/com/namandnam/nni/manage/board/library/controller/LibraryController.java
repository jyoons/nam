package com.namandnam.nni.manage.board.library.controller;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.constant.BoardTypeEnum;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.vo.Params;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.manage.board.library.service.LibraryService;
import com.namandnam.nni.manage.board.library.vo.Library;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/manage/board/library")
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

        return "manage/board/library/list";
    }

    @PostMapping("/list-data")
    public String listData(ModelMap model, Library library, HttpServletRequest req) {

        library.setBoardCtgy( BoardTypeEnum.NOTICE.getCode() );
        Result result = libraryService.selectList(library);

        model.addAttribute(Const.KEY_RESULT, result);

        return "manage/board/library/list-data";
        //return viewPath("/community/board_list", req);
    }

}
