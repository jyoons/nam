package com.namandnam.nni.common.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.namandnam.nni.common.vo.SampleVo;
import com.namandnam.nni.common.web.Const;

@Component("excelDownload")
public class ExcelView extends AbstractXlsView{
	protected void buildExcelDocument(Map<String,Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sCurTime = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(new Date());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + sCurTime + ".xls\"");
		Sheet worksheet = null;
		Row row = null;
		List<SampleVo> dscntList = null;
		
		Map<String, Object> mapData = (Map)model.get("list");
		List<String> head = (List) model.get("head");
		if(head.size() == 13) {
			dscntList = (List) mapData.get(Const.KEY_ITEM); // 새로운 sheet를 생성한다. 
		} else {
//			lwstList = (List) mapData.get(Const.KEY_ITEM); // 새로운 sheet를 생성한다. 
		}
		int totalCount = (int)mapData.get("count");
		
		CellStyle numberCellStyle = workbook.createCellStyle(); 
		DataFormat numberDataFormat = workbook.createDataFormat(); 
		numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));

		worksheet = workbook.createSheet("대리점 할인가격"); // 가장 첫번째 줄에 제목을 만든다. 
		row = worksheet.createRow(0);
		
		for(int i = 0 ; i < head.size(); i++) {
			row.createCell(i).setCellValue(head.get(i));
		}
		
		int rowidx = 1;
		for(SampleVo dscntPrice : dscntList) {
			row = worksheet.createRow(rowidx);
			row.createCell(0).setCellValue(totalCount - rowidx + 1);
			row.createCell(1).setCellValue(dscntPrice.getTitle());
			row.createCell(2).setCellValue(dscntPrice.getContents());
			row.createCell(3).setCellValue(dscntPrice.getIdx());
			row.getCell(3).setCellStyle(numberCellStyle);
			rowidx++;
		}
		
		
	}
}
