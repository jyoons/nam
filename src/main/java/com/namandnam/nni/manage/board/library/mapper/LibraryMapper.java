package com.namandnam.nni.manage.board.library.mapper;

import com.namandnam.nni.manage.board.library.vo.Library;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 파 일 명 : LibraryMapper
 * 작 성 자 : smlee
 * 날    짜 : 2021-10-14 오전 11:23
 * 설    명 : 메인 > 소식 & 자료 > 자료실 Mapper
 */
@Mapper
public interface LibraryMapper {

    List<Library> selectList(Library library);

    int selectListCnt(Library library);

}
