package com.namandnam.nni.common.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.namandnam.nni.common.utils.CommUtil;
import com.namandnam.nni.common.utils.Encrypt;
import com.namandnam.nni.common.vo.Params;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.namandnam.nni.common.vo.Emails;
import com.namandnam.nni.common.vo.Result;
import com.namandnam.nni.common.vo.SampleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 테스트 페이지.
 * @author dhkim
 *
 */
@Slf4j
@RequestMapping("/sample")
@Controller
@RequiredArgsConstructor
public class SampleController extends CommonController {
	
	private final SampleService sampleService;
	
	
	private void gitTest() {
		log.info("a");
		log.info("b");
		log.info("c");
		log.info("d");
		log.info("e");
		log.info("ff");
		log.info("gg");
	}
	
	/**
	 * 등록 페이지 이동
	 * @return
	 */
	@GetMapping("/addpage")
	public String addData(ModelMap model, Params params, HttpServletRequest req) {

		log.debug(params.toString());
		
		model.addAttribute("TEST" , "SAMPLE");
		model.addAttribute(Const.KEY_PARAMS, params);

		return viewPath("/sample/sample_add", req);
	}
	
	/**
	 * invest 게시판 등록
	 * @return
	 */
	@PostMapping("/addData.do")
	@ResponseBody
	public Result addAction(ModelMap model, SampleVo sv , MultipartHttpServletRequest mreq) {
		
		
		if (mreq != null) {
			//file save
			MultipartFile mfile = mreq.getFile("filea");
			if (mfile != null && !mfile.getOriginalFilename().isEmpty()) {
				log.debug( mfile.getName() );
				
				try {
					sampleService.insertIntoSampleDataAndFile(sv, mfile);
				} catch (Exception e) {
					// TODO: handle exception
					return Result.builder().success(false).build();
				}
				
				
			}else {
				sampleService.insertIntoSampleData(sv);
			}
		}
		
		return Result.builder().success(true).build();
	}
	
	/**
	 * 수정 페이지 이동
	 * @return
	 */
	@GetMapping("/view/{sampleIdx}")
	public String viewData(ModelMap model, HttpServletRequest req, @PathVariable String sampleIdx) {
		
		SampleVo sampleData = sampleService.selectOneSampleData(sampleIdx);
		
		if( sampleData == null || sampleData.getTitle() == null || sampleData.getTitle().trim().equals("") ){
			 return "redirect:/dshbr/main";
		}
		
		log.debug( sampleData.toString() );
		
		model.addAttribute(Const.KEY_ITEM, sampleService.selectOneSampleData(sampleIdx));

		return viewPath("/sample/sample_add", req);
	}
	
	
	
	/**
	 * sendGmailTest
	 * @return
	 */
	@GetMapping("/sendGmailTest/{toMail}")
	@ResponseBody
	public String sendGmailTest(ModelMap model, HttpServletRequest req, @PathVariable String toMail) {
		
		CommUtil.fnsendMail( Emails.builder().strfromMail("dhkim@reflexion.co.kr")
									.strToMail(toMail)
									.strTitle("테스트 제목")
									.strMsg("메일 테스트 입니다.")
									.build() );
		
		
		
		return "TEST";
	}
	
	/**
	 * sha256 returun sample
	 * @return
	 */
	@GetMapping("/get/sha256/{sample}")
	@ResponseBody
	public String getSha256(ModelMap model, HttpServletRequest req, @PathVariable String sampleStr ) {
		
		return Encrypt.sha256Hex( sampleStr ) ;

	}
	
	
	@PostMapping(path="/excelDownload", produces="application/vnd.ms-excel")
	public String excelDownload(HttpServletRequest req, HttpServletResponse res, Model model) {
		List<SampleVo> list = sampleService.selectSampleList( SampleVo.builder().build() );
		List<String> head = new ArrayList<>();
		head.add("NO");
		head.add("상품명");
		head.add("업체명");
		head.add("판매가");
		head.add("정가");
		head.add("할인율");
		head.add("반품주소");
		head.add("연락처");
		head.add("대표명");
		head.add("이메일");
		head.add("사업자등록번호");
		head.add("처리상태");
		head.add("메모");
		model.addAttribute("list", list);
		model.addAttribute("head", head);
		return "excelDownload";
	}
	
	
	
}
