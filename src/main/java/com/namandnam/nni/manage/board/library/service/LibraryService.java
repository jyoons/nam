package com.namandnam.nni.manage.board.library.service;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.web.Const;
import com.namandnam.nni.manage.board.library.mapper.LibraryMapper;
import com.namandnam.nni.manage.board.library.vo.Library;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    public Result selectList(Library library) {
        int totalRecordCount = 0;

        totalRecordCount = (Integer) libraryMapper.selectListCnt(library);
        List<Library> list = libraryMapper.selectList(library);

        library.setTotalRecordCount(totalRecordCount);
        library.paginate();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put( Const.KEY_ITEM, list);
        data.put( "count", totalRecordCount);
        data.put( Const.KEY_PARAMS, library);

        return Result.builder().data(data).success(true).build();
    }
}
