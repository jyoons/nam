package com.namandnam.nni.manage.library.service;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.manage.library.mapper.LibraryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 파 일 명 : LibraryService
 * 작 성 자 : smlee
 * 날    짜 : 2021-10-14 오전 11:20
 * 설    명 : 메인 > 소식 & 자료 > 자료실 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    @Autowired
    private UserSessionScopeBean sessionBean;

    private final LibraryMapper libraryMapper;
    private final CommonMapper commonMapper;
    private final FileService fileService;
}
