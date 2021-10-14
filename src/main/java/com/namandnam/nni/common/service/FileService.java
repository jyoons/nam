/*
 * Copyright yysvip.tistory.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of yysvip.tistory.com.,LTD. ("Confidential Information").
 */
package com.namandnam.nni.common.service;

import java.io.File;
import java.io.IOException;

import com.namandnam.nni.common.utils.CommUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.namandnam.nni.common.mapper.CommonMapper;
import com.namandnam.nni.common.utils.OSValidator;
import com.namandnam.nni.common.vo.AttachFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

	@Value("${env.file.path}")
	private String prefix_upload_path;
	
	private final CommonMapper commonMapper;
	
	/**
	 * 
	 * @param fileIdx
	 * @return
	 * @throws Exception
	 */
	public AttachFile selectOneByIdx( String fileIdx ) throws Exception {
		
		log.debug(" fileIdx : " + fileIdx );
		return commonMapper.selectOneByIdx( fileIdx );
	}
	
	/**
	 * 
	 * @param fileIdx
	 * @return
	 * @throws Exception
	 */
	public AttachFile selectOneFileInfo( String fileIdx ) throws Exception {
		
		log.debug(" fileIdx : " + fileIdx );
		return commonMapper.selectOneFileInfo( fileIdx );
	}
	
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public AttachFile saveFileInfo( MultipartFile file ) throws Exception {
		
		return saveFileInfo( file, null );
	}
	
	/**
	 * 
	 * @param file
	 * @param subPath
	 * @return
	 * @throws Exception
	 */
	public AttachFile saveFileInfo( MultipartFile file, String subPath ) throws Exception {
		
		String[] validExt = {"gif","png","jpg","jpeg","doc","docx","xls","xlsx","hwp","pdf","ppt","pptx","zip","mp4"};
		//String[] validType = {"", "", ""};
		
		boolean isCan = false;
		AttachFile attach = new AttachFile();
		
		String originalFileName = null;
        String originalFileExtension = null;
        String originalFileContentType = null;
        String originalFileSize = null;
        String storedFileName = null;
        
        if (file != null && !file.getOriginalFilename().isEmpty()) {
        	try {
        		String yyymm = subPath == null ? CommUtil.getYYYYMM(0) : subPath;
	        	String filePath = prefix_upload_path + File.separator + yyymm;
	        	
        		File dirs = new File(filePath);
	        	if (dirs.exists() == false) {
	        		dirs.mkdirs();
	        		if( !OSValidator.isWindows() ) {
	        			Runtime.getRuntime().exec("chmod -R 777 " + filePath);
	        		}
	        	}
	        	
	        	//byte[] bytes = file.getBytes();
	        	originalFileName = CommUtil.cleanFName(file.getOriginalFilename());
	        	originalFileExtension = (FilenameUtils.getExtension(originalFileName)).toLowerCase();
	        	originalFileSize = "" + file.getSize();
	        	originalFileContentType = file.getContentType();
	        	
	        	for (int i = 0; i < validExt.length; i++) {
	        		if (originalFileExtension.equalsIgnoreCase(validExt[i])) {
	        			isCan = true;
	        			break;
	        		}
	        	}
	        	
	        	if (isCan) {
	        		
		        	storedFileName = CommUtil.getUUID() + "." + originalFileExtension;
		        	
		        	attach.setFileType( CommUtil.nvl(originalFileContentType) );
		        	attach.setFileName( CommUtil.nvl(storedFileName) );
		        	attach.setOrgnlFileName( CommUtil.nvl(originalFileName) );
		        	attach.setFileSize( originalFileSize );
		        	attach.setFilePath( filePath );
		        	
		        	attach.setSubPath( yyymm );	//editor에서 사용하기 위해서
		
		        	String uploadPath = filePath + File.separator +  storedFileName;
		        	
		        	file.transferTo(new File(uploadPath));
	        	}
        	} catch(IOException e) {
        		log.error("fail to process file", e);
        		throw e;
        	} finally {
        	}
        }
        return attach;
	}
	
	
	/**
	 * 
	 * @param fileIdx
	 * @return
	 * @throws Exception
	 */
	public int deleteFileInfo( String fileIdx ){
		
		return commonMapper.deleteAttachFile( fileIdx );
	}
	

}
