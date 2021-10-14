package com.namandnam.nni.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.namandnam.nni.common.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.namandnam.nni.common.vo.AttachFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/file")
public class FileController {
	
	private final FileService fileService;
	
	@RequestMapping(value = "/editorImageUpload.do", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, Object> communityImageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {
		
        HashMap<String, Object> rt = new HashMap<String, Object>();

        try {
    		
    		AttachFile af = fileService.saveFileInfo(upload, "public");
            
            String fileUrl = "/attach/" + af.getSubPath() + "/" + af.getFileName();

            rt.put("filename", af.getFileName());
            rt.put("uploaded", 1);
            rt.put("url", fileUrl);
            
        } catch(IOException e) {
            e.printStackTrace();
        } 

		return rt;
    }
	
	
	
	@RequestMapping(value="/download.do")
	public void donwloadPage(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileIdx") String fileIdx) throws Exception {
		
		try {
			
			//TODO fileIdx 기준으로 정보를 경로를 가져온다.
			AttachFile af = fileService.selectOneFileInfo(fileIdx);
			
			if( af != null && !af.getFileName().equals("") ) {
			
				String filePath = af.getFilePath() + File.separator + af.getFileName();
				System.out.println(filePath);
				
				File downloadFile = new File(filePath);					
				ServletOutputStream outStream = null;
				FileInputStream inputStream = null;
				
				//파일이 존재 할 경우
				if(downloadFile.exists()) {
					try {
						
						outStream = response.getOutputStream();
						inputStream = new FileInputStream(downloadFile);
						
						String downFileName = java.net.URLEncoder.encode( af.getOrgnlFileName(), "UTF-8");
						
						response.setContentType("application/octet-stream");
						response.setContentLength((int)downloadFile.length());
						response.setHeader("Content-Disposition", "attachment; fileName=\"" + downFileName +"\";");
					    response.setHeader("Content-Transfer-Encoding", "binary");
						//Writing InputStream to OutputStream
						byte[] outByte = new byte[4096];
						while(inputStream.read(outByte, 0, 4096) != -1){
							outStream.write(outByte, 0, 4096);
						}
					} catch (Exception ee) {
						ee.printStackTrace();
					} finally {
						inputStream.close();
						outStream.flush();
						outStream.close();
					}						
				}//file exist
				else {
					log.debug("NO FILE");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
	
	
	
	@GetMapping("/fileDown.do")
	public void downloadFile(@RequestParam("fileIdx") String idx, HttpServletResponse response) {
		try {
			AttachFile attachFile = fileService.selectOneByIdx(idx);
		    byte fileByte[] = FileUtils.readFileToByteArray(new File(attachFile.getFilePath() + File.separator + attachFile.getFileName()));
		     
		    response.setContentType("application/octet-stream");
		    response.setContentLength(fileByte.length);
		    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(attachFile.getOrgnlFileName(),"UTF-8")+"\";");
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    response.getOutputStream().write(fileByte);
		     
		    response.getOutputStream().flush();
		    response.getOutputStream().close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
