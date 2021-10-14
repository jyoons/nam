package com.namandnam.nni.common.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.service.FileService;
import com.namandnam.nni.common.vo.AttachFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.namandnam.nni.common.vo.SampleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {

    private final CommonMapper commonMapper;
    
    private final FileService fileService;
    
    
    public SampleVo selectOneSampleData(String idx) {
    	return commonMapper.selectOneSampleData(idx);
    }
    
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Boolean insertIntoSampleDataAndFile(SampleVo sv, MultipartFile upload) throws Exception {
    	
    	commonMapper.insertIntoSampleData(sv);
    	
    	try {
			AttachFile af = fileService.saveFileInfo(upload);
			af.setTblCtgry( "sample" );
			af.setTblIdx( sv.getIdx() );
			
			int aaa = commonMapper.insertIntoAttachFile( af );
			
			log.debug(aaa+"");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("fail to process file", e);
			throw e;
		}
    	
    	return true;
    }
    
    public int insertIntoSampleData(SampleVo sv) {
    	
    	return commonMapper.insertIntoSampleData(sv);
    	
    }

    /**
     * 게시판 리스트
     * @param board
     */
//    public Result selectList(Board board) {
//    	
//    	int totalRecordCount = 0;
//    	board.setRecordCountPerPage(100);
//	    totalRecordCount = (Integer) boardMapper.selectListCnt(board);
//	    
//	    List<Board> list = (List<Board>) boardMapper.selectList(board);
//	    
//	    board.setTotalRecordCount(totalRecordCount);
//	    board.paginate();
//
//	    Map<String, Object> data = new HashMap<String, Object>(); 
//	    data.put( Const.KEY_ITEM, list);
//	    data.put( "count", totalRecordCount);
//	    data.put( Const.KEY_PARAMS, board);
//	    
//	    return Result.builder().data(data).success(true).build();
//    	
//    }
    
    
    /**
     * sampleList
     * @param board
     */
    @SuppressWarnings("rawtypes")
	public List selectSampleList(SampleVo sv) {
    	
    	//commonMapper.insertIntoSampleData(sv);
    	
    	
    	List<String> array = new ArrayList<String>();
    	array.add("20201210");
    	array.add("20201209");
    	array.add("20201208");
    	//array.add("20201207");
    	//array.add("20201206");
    	
    	List<Map<String, Object>> list = commonMapper.selectSampleList(array);
    	
    	
    	return list;
    }

}
