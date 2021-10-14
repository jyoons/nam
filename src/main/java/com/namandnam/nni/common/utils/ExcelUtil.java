package com.namandnam.nni.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;


/**
 * <PRE>
 * 설명 : 엑셀파일 읽기, 다운로드 기능을 지원합니다.
 * 
 * Office 2007 부터 확장자명이 xlsx로 변경 되면서 jxl을 이용하여 데이터를 읽어 들일수 없음.
 * 
 * org.apache.poi 사용시 메모리를 효율적으로 사용하지 못한다는 단점이 있음
 * com.hongli.inaschool.util
 * </PRE>
 *
 */
public class ExcelUtil {
	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
	private static final int MAX_LINE = 100000;

	/**
	 * 
	 * xls 또는 xlsx 여부를 체크한다.
	 * 
	 * @param uploadFile
	 * @return
	 */
	static public boolean isOldExcelFile(MultipartFile uploadFile) {
        try {
            String contentsType = uploadFile.getContentType();
            if (!"application/vnd.ms-excel".equals(contentsType)) {
                return false;
            }
        } catch (Exception e) {
            log.debug(e.toString());
            return false;
        }
	    
	    return true;
	}
	
	/**
	 * 
	 * 파일객체의 속성이 엑셀파일인가를 판단한다. 
	 * 
	 * 
	 * @param uploadFile
	 * @return
	 */
	static public boolean isExcelFile(MultipartFile uploadFile) {
	    if (null != uploadFile) {
	        try {
	            String contentsType = uploadFile.getContentType();
	            if (   contentsType.equals("application/vnd.ms-excel")
	                ||  contentsType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") 
	                    
	            ) {
	                if (isOldExcelFile(uploadFile)) {
	                    return isExcelFile(uploadFile.getInputStream());
	                } else {
	                    return isExcelFile2(uploadFile.getInputStream());
	                }
	                
	            }
            } catch (Exception e) {
                log.debug(e.toString());
                return false;
            }
	    }
	    
	    return false;
	}
    /**
     * 
     * 정상의 xls 파일 여부를 체크한다. 
     * 
     * @param inp
     * @return
     */
	static public boolean isExcelFile(InputStream inp) {
	    HSSFWorkbook wb = null;
	    POIFSFileSystem fs = null;
	    try {
	        if (null != inp && inp.hashCode() > 0) {
	            fs = new POIFSFileSystem(inp);
	            wb = new HSSFWorkbook(fs);
	            if (null != wb && wb.getNumberOfSheets() >= 1) {
	                return true;
	            }
	        }
            return false;
        } catch (Exception e) {
           log.debug(e.toString());
           System.out.println(e.getMessage());
           System.out.println(e.getClass().toString());
           return false;
        } finally {
            try {
                if (null != inp) {
                    inp.close();
                }
                if (null != wb) {
                    wb = null;
                }
                if (null != fs) {
                    fs = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 
	 * 정상의 xlsx 파일 여부를 체크한다. 
	 * 
	 * @param inp
	 * @return
	 */
    static public boolean isExcelFile2(InputStream inp) {
        XSSFWorkbook wb = null;
        try {
            if (null != inp && inp.hashCode() > 0) {
                wb = new XSSFWorkbook(inp);
                if (null != wb && wb.getNumberOfSheets() >= 1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
           log.debug(e.toString());
           System.out.println(e.getMessage());
           System.out.println(e.getClass().toString());
           return false;
        } finally {
            try {
                if (null != inp) {
                    inp.close();
                }
                if (null != wb) {
                    wb = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }	
	
    /**
     * 엑셀파일에서 데이터를 읽어들여서 Json 문자 로 변화하여 리턴함 ** 주의 [데이터행 시작 번호 , 데이터행 끝번호
     * ]는 short 로 변환 하기때문에 -32768～32767 범위 내에서 셋을 해서 기동하여하 한다
     * 
     * @param xFile
     *            엑셀파일
     * @param sheetNum
     *            데이터시트번호
     * @param startRow
     *            데이터열 시작 번호
     * @param startCol
     *            데이터행 시작 번호
     * @param startCol
     *            데이터행 끝번호
     * @param jsonMappingKey
     *            결과 맵핑키
     * @return
     * @throws Exception
     */
	static public String exceleToJsonString(InputStream in, int sheetNum, int startRow, int startCol, int endCol , String jsonMappingKey, String[] colrumNames) {
	    String jsonString = null;
	    List<Map<String, String>>  dataList = xlsRead(in, sheetNum, startRow, startCol, endCol, colrumNames);
	    Map<String , Object> rstMap = new HashMap<String, Object>();
	    rstMap.put(jsonMappingKey, dataList);
	   
	    Gson gson = new Gson();
	    jsonString = gson.toJson(rstMap);
	    
	    return jsonString;
	}
	
	static public List<Map<String, String>> exceleRead(MultipartFile uploadFile, int sheetNum, int startRow, int startCol, int endCol, String[] colrumNames) throws Exception {
	    if (isOldExcelFile(uploadFile)) {
	        return xlsRead(uploadFile.getInputStream() , sheetNum, startRow, startCol, endCol, colrumNames);	        
	    } else {
	        return xlsxRead(uploadFile.getInputStream() , sheetNum, startRow, startCol, endCol, colrumNames);
	    }

	}
	
	 /**
     * xls일에서 데이터를 읽어들여서 리스트 (리스트 (문자)) 로 변화하여 리턴함 ** 주의 [데이터행 시작 번호 , 데이터행 끝번호
     * ]는 short 로 변환 하기때문에 -32768～32767 범위 내에서 셋을 해서 기동하여하 한다
     * 
     * @param xFile
     *            엑셀파일 스트림
     * @param sheetNum
     *            데이터시트번호
     * @param startRow
     *            데이터열 시작 번호
     * @param startCol
     *            데이터행 시작 번호
     * @param startCol
     *            데이터행 끝번호
     * @return
     * @throws Exception
     */
	static public List<Map<String, String>> xlsRead(InputStream in, int sheetNum, int startRow, int startCol, int endCol, String[] colrumNames) {
        // Excel 파일 불러오기
	    List<Map<String, String>>  rst = new ArrayList<Map<String,String>>();
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            fs = new POIFSFileSystem(in);
            // 워크북 오브젝트의 취득
            wb = new HSSFWorkbook(fs);
            if (wb.getNumberOfSheets() < 1) {
                return rst;
            }

            // 워크시트를 표시하는 오브젝트의 취득 （1）
            // 데이터시트지정
            HSSFSheet sheet = wb.getSheetAt(sheetNum);

            // 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
            int lastRow = sheet.getLastRowNum();

            // 행 별로 데이터를 취득
            addRow:
            for (int rowIdx = startRow; rowIdx <= lastRow; rowIdx++) {
                if (rowIdx >= MAX_LINE + startRow) {
                    break;
                }
                

                // 행을 표시하는 오브젝트의 취득
                HSSFRow row = sheet.getRow(rowIdx);
                Map<String, String> lineData = new LinkedHashMap<String, String>();
                // 행에 데이터가 없는 경우
                if (row == null) {
                    break;
                }

                // 행에서 첫셀과 마지막 셀의 인덱스를 취득
                short firstCell = (short) startCol;
                short lastCell = (short) endCol;

                // 셀 별로 데이터를 취득
                int colIdx = 0;
                for (int cellIdx = firstCell; cellIdx <= lastCell; cellIdx++) {
                    String data = "";

                    // 셀을 표시하는 오브젝트를 취득
                    HSSFCell cell = row.getCell(cellIdx);

                    // 빈 셀인 경우
                    if (cell == null) {
                        //셀의 첫행이 비어있을경우 종료
                        if (cellIdx == firstCell){
                            break addRow;
                        } else {
                            lineData.put(colrumNames[colIdx] ,null);
                            colIdx ++;
                            continue;
                        }
                    }

                    // 셀에 있는 데이터의 종류를 취득
                    int type = cell.getCellType();

                    // 데이터 종류별로 데이터를 취득
                    switch (type) {
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        boolean bdata = cell.getBooleanCellValue();
                        data = String.valueOf(bdata);
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        int ddata = (int) cell.getNumericCellValue();
                        data = String.valueOf(ddata);
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        data = cell.getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        //셀의 첫행이 비어있을경우 종료
                        if (cellIdx == firstCell){
                            break addRow;
                        } else {
                            data = null;
                            break;
                        }
                    case HSSFCell.CELL_TYPE_ERROR:
                    case HSSFCell.CELL_TYPE_FORMULA:
                    default:
                        continue;
                    }
                    lineData.put(colrumNames[colIdx] ,data);
                    colIdx ++;
                }
                rst.add(lineData);
                
            }
            return rst;     
        } catch (Exception e) {
            log.debug(e.toString());
            return null;
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != wb) {
                    wb = null;
                }
                if (null != fs) {
                    fs = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	
    /**
    * xlsx 파일에서 데이터를 읽어들여서 리스트 (리스트 (문자)) 로 변화하여 리턴함 ** 주의 [데이터행 시작 번호 , 데이터행 끝번호
    * ]는 short 로 변환 하기때문에 -32768～32767 범위 내에서 셋을 해서 기동하여하 한다
    * 
    * @param xFile
    *            엑셀파일 스트림
    * @param sheetNum
    *            데이터시트번호
    * @param startRow
    *            데이터열 시작 번호
    * @param startCol
    *            데이터행 시작 번호
    * @param startCol
    *            데이터행 끝번호
    * @return
    * @throws Exception
    */
    static public List<Map<String, String>> xlsxRead(InputStream in, int sheetNum, int startRow, int startCol, int endCol, String[] colrumNames) {
        // Excel 파일 불러오기
        List<Map<String, String>>  rst = new ArrayList<Map<String,String>>();
        XSSFWorkbook wb = null;
        try {
            // 워크북 오브젝트의 취득
            wb = new XSSFWorkbook(in);
            if (wb.getNumberOfSheets() < 1) {
                return rst;
            }

            // 워크시트를 표시하는 오브젝트의 취득 （1）
            // 데이터시트지정
            XSSFSheet sheet = wb.getSheetAt(sheetNum);

            // 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
            int lastRow = sheet.getLastRowNum();
            //워크 시트 이름 가져오기
            String sheetNm = sheet.getSheetName() ;
            System.out.println("sheet Name >> " + sheetNm) ;

            // 행 별로 데이터를 취득
            addRow:
            for (int rowIdx = startRow; rowIdx <= lastRow; rowIdx++) {
                if (rowIdx >= MAX_LINE + startRow) {
                    break;
                }
                

                // 행을 표시하는 오브젝트의 취득
                XSSFRow row = sheet.getRow(rowIdx);
                Map<String, String> lineData = new LinkedHashMap<String, String>();
                // 행에 데이터가 없는 경우
                if (row == null) {
                    break;
                }
                lineData.put("sheetNm", sheetNm) ;
                // 행에서 첫셀과 마지막 셀의 인덱스를 취득
                short firstCell = (short) startCol;
                short lastCell = (short) endCol;

                // 셀 별로 데이터를 취득
                int colIdx = 0;
                for (int cellIdx = firstCell; cellIdx <= lastCell; cellIdx++) {
                    String data = "";

                    // 셀을 표시하는 오브젝트를 취득
                    XSSFCell cell = row.getCell(cellIdx);

                    // 빈 셀인 경우
                    if (cell == null) {
                        //셀의 첫행이 비어있을경우 종료
                        if (cellIdx == firstCell){
                            break addRow;
                        } else {
                            lineData.put(colrumNames[colIdx] ,null);
                            colIdx ++;
                            continue;
                        }
                    }

                    // 셀에 있는 데이터의 종류를 취득
                    int type = cell.getCellType();

                    // 데이터 종류별로 데이터를 취득
                    switch (type) {
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        boolean bdata = cell.getBooleanCellValue();
                        data = String.valueOf(bdata);
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        int ddata = (int) cell.getNumericCellValue();
                        data = String.valueOf(ddata);
                        break;
                    case XSSFCell.CELL_TYPE_STRING:
                        data = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_BLANK:
                        //셀의 첫행이 비어있을경우 종료
                        if (cellIdx == firstCell){
                            break addRow;
                        } else {
                            data = null;
                            break;
                        }
                    case XSSFCell.CELL_TYPE_ERROR:
                    case XSSFCell.CELL_TYPE_FORMULA:
                    default:
                        continue;
                    }
                    lineData.put(colrumNames[colIdx] ,data);
                    colIdx ++;
                }
                rst.add(lineData);
                
            }
            return rst;     
        } catch (Exception e) {
            log.debug(e.toString());
            return null;
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != wb) {
                    wb = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }	
	
	/**
	 * 엑셀파일에서 데이터를 읽어들여서 리스트 (리스트 (문자)) 로 변화하여 리턴함 ** 주의 [데이터행 시작 번호 , 데이터행 끝번호
	 * ]는 short 로 변환 하기때문에 -32768～32767 범위 내에서 셋을 해서 기동하여하 한다
	 * 
	 * @param xFile
	 *            엑셀파일
	 * @param sheetNum
	 *            데이터시트번호
	 * @param startRow
	 *            데이터열 시작 번호
	 * @param startCol
	 *            데이터행 시작 번호
	 * @param startCol
	 *            데이터행 끝번호
	 * @return
	 * @throws Exception
	 */
    static public List<List<String>> exceleRead(File xFile, int sheetNum, int startRow, int startCol, int endCol) throws IllegalStateException{
		// Excel 파일 불러오기
		List<List<String>> rst = new ArrayList<List<String>>();
		int fNm = xFile.getName().indexOf("xlsx") ;
	
		if( fNm < 0){
			rst = xlsExcelRead(xFile, sheetNum, startRow , startCol , endCol) ;
		}else if( fNm > 0){
			rst = xlsxExcelRead(xFile, sheetNum, startRow , startCol , endCol) ;
		}
	
		return rst;
	}
    
    private static List<List<String>> xlsExcelRead(File xFile, int sheetNum, int startRow, int startCol, int endCol) throws IllegalStateException{
    	// Excel 파일 불러오기
		List<List<String>> rst = new ArrayList<List<String>>();
		InputStream in = null;
		HSSFWorkbook wb = null;
		POIFSFileSystem fs = null;
		try {
			in = new FileInputStream(xFile);
			fs = new POIFSFileSystem(in);
			// 워크북 오브젝트의 취득
			wb = new HSSFWorkbook(fs);
			if (wb.getNumberOfSheets() < 1) {
				return rst;
			}

			// 워크시트를 표시하는 오브젝트의 취득 （1）
			// 데이터시트지정
			HSSFSheet sheet = wb.getSheetAt(sheetNum);

			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			int lastRow = sheet.getLastRowNum();

			// 행 별로 데이터를 취득
			for (int rowIdx = startRow; rowIdx <= lastRow; rowIdx++) {
				if (rowIdx >= MAX_LINE + startRow) {
					break;
				}
				
				List<String> lineData = new ArrayList<String>();
				// 행을 표시하는 오브젝트의 취득
				HSSFRow row = sheet.getRow(rowIdx);
			//	rst.add(wb.getSheetName(sheetNum)) ;
				// 행에 데이터가 없는 경우
				if (row == null) {
					break;
				}

				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				short firstCell = (short) startCol;
				short lastCell = (short) endCol;

				// 셀 별로 데이터를 취득
				for (int cellIdx = firstCell; cellIdx <= lastCell; cellIdx++) {
					String data = "";

					// 셀을 표시하는 오브젝트를 취득
					HSSFCell cell = row.getCell(cellIdx);

					// 빈 셀인 경우
					if (cell == null) {
						lineData.add(null);
						continue;
					}

					// 셀에 있는 데이터의 종류를 취득
					int type = cell.getCellType();
					
					// 데이터 종류별로 데이터를 취득
					switch (type) {
					case HSSFCell.CELL_TYPE_BOOLEAN:
						boolean bdata = cell.getBooleanCellValue();
						data = String.valueOf(bdata);
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						int ddata = (int) cell.getNumericCellValue();
						data = String.valueOf(ddata);
						break;
					case HSSFCell.CELL_TYPE_STRING:
						data = cell.getStringCellValue();
						break;
					/*case HSSFCell.CELL_TYPE_BLANK:
						//셀의 첫행이 비어있을경우 종료
						if (cellIdx == firstCell){
							break addRow;
						} else {
							data = null;
							break;
						}*/
					case HSSFCell.CELL_TYPE_ERROR:
					case HSSFCell.CELL_TYPE_FORMULA:
					default:
						continue;
					}
					lineData.add(data);
				}

				rst.add(lineData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			throw new IllegalStateException("TYPE_ERROR");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return rst;
    }
    private static List<List<String>> xlsxExcelRead(File xFile, int sheetNum, int startRow, int startCol, int endCol) throws IllegalStateException{
    	// Excel 파일 불러오기
		List<List<String>> rst = new ArrayList<List<String>>();
		InputStream in = null;
		XSSFWorkbook wb = null;
		try {
			in = new FileInputStream(xFile);
			// 워크북 오브젝트의 취득
			wb = new XSSFWorkbook(in);
			if (wb.getNumberOfSheets() < 1) {
				return rst;
			}
			// 워크시트를 표시하는 오브젝트의 취득 （1）
			// 데이터시트지정
			XSSFSheet sheet = wb.getSheetAt(sheetNum);
			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			int lastRow = sheet.getLastRowNum();
			//int lastRow = sheet.getPhysicalNumberOfRows() ;

			// 행 별로 데이터를 취득
			for (int rowIdx = startRow; rowIdx <= lastRow; rowIdx++) {
				if (rowIdx >= MAX_LINE + startRow) {
					break;
				}
				
				List<String> lineData = new ArrayList<String>();
				// 행을 표시하는 오브젝트의 취득
				XSSFRow row = sheet.getRow(rowIdx);
			//	rst.add(wb.getSheetName(sheetNum)) ;
				// 행에 데이터가 없는 경우
				if (row == null) {
					break;
				}

				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				short firstCell = (short) startCol;
				short lastCell = (short) endCol;

				// 셀 별로 데이터를 취득
				for (int cellIdx = firstCell; cellIdx <= lastCell; cellIdx++) {
					String data = "";

					// 셀을 표시하는 오브젝트를 취득
					XSSFCell cell = row.getCell(cellIdx);

					// 빈 셀인 경우
					if (cell == null) {
						lineData.add("");
						continue;
					}

					// 셀에 있는 데이터의 종류를 취득
					int type = cell.getCellType();

					// 데이터 종류별로 데이터를 취득
					switch (type) {
					case HSSFCell.CELL_TYPE_BOOLEAN:
						boolean bdata = cell.getBooleanCellValue();
						data = String.valueOf(bdata);
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						int ddata = (int) cell.getNumericCellValue();
						data = String.valueOf(ddata);
						break;
					case HSSFCell.CELL_TYPE_STRING:
						data = cell.getStringCellValue();
						break;
					/*case HSSFCell.CELL_TYPE_BLANK:
						//셀의 첫행이 비어있을경우 종료
						if (cellIdx == firstCell){
							break addRow;
						} else {
							data = null;
							break;
						}*/
					case HSSFCell.CELL_TYPE_ERROR:
					case HSSFCell.CELL_TYPE_FORMULA:
					default:
						continue;
					}
					lineData.add(data);
				}
				rst.add(lineData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			throw new IllegalStateException("TYPE_ERROR");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return rst;
    }
    
    // 교직원 및 학생관리 업로드용
    static public List<List<String>> exceleRead2(InputStream in, String fileName, int sheetNum, int startRow, int startCol, int endCol) {
		// Excel 파일 불러오기
		List<List<String>> rst = new ArrayList<List<String>>();
		int fNm = fileName.indexOf("xlsx") ;
	
		if( fNm < 0){
			rst = xlsExcelRead2(in, sheetNum, startRow , startCol , endCol) ;
		}else if( fNm > 0){
			rst = xlsxExcelRead2(in, sheetNum, startRow , startCol , endCol) ;
		}
	
		return rst;
	}
    
    private static List<List<String>> xlsExcelRead2(InputStream in, int sheetNum, int startRow, int startCol, int endCol){
    	// Excel 파일 불러오기
		List<List<String>> rst = new ArrayList<List<String>>();
		HSSFWorkbook wb    = null;
		POIFSFileSystem fs = null;
		
		try {
			fs = new POIFSFileSystem(in);
			// 워크북 오브젝트의 취득
			wb = new HSSFWorkbook(fs);
			if (wb.getNumberOfSheets() < 1) {
				return rst;
			}

			// 워크시트를 표시하는 오브젝트의 취득 （1）
			// 데이터시트지정
			HSSFSheet sheet = wb.getSheetAt(sheetNum);

			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			int lastRow = sheet.getLastRowNum();

			// 행 별로 데이터를 취득
			for (int rowIdx = startRow; rowIdx <= lastRow; rowIdx++) {
				if (rowIdx >= MAX_LINE + startRow) {
					break;
				}
				
				List<String> lineData = new ArrayList<String>();
				// 행을 표시하는 오브젝트의 취득
				HSSFRow row = null;
				try {
					row = sheet.getRow(rowIdx);
				} catch (IndexOutOfBoundsException ie) {
					log.debug(ie.toString());
					continue;
				}
			//	rst.add(wb.getSheetName(sheetNum)) ;
				// 행에 데이터가 없는 경우
				if (row == null) {
					break;
				}

				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				short firstCell = (short) startCol;
				short lastCell  = (short) endCol;
				// 셀 별로 데이터를 취득
				for (int cellIdx = firstCell; cellIdx <= lastCell; cellIdx++) {
					String data = "";

					// 셀을 표시하는 오브젝트를 취득
					HSSFCell cell = row.getCell(cellIdx);

					// 빈 셀인 경우
					if (cell == null) {
						lineData.add(null);
						continue;
					}

					// 셀에 있는 데이터의 종류를 취득
					int type = cell.getCellType();
					
					// 데이터 종류별로 데이터를 취득
					switch (type) {
					case HSSFCell.CELL_TYPE_BOOLEAN:
						boolean bdata = cell.getBooleanCellValue();
						data = String.valueOf(bdata);
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						int ddata = (int) cell.getNumericCellValue();
						data = String.valueOf(ddata);
						break;
					case HSSFCell.CELL_TYPE_STRING:
						data = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BLANK:
						//셀의 첫행이 비어있을경우 종료
						//if (cellIdx == firstCell){
						//	break addRow;
						//} else {
							data = null;
							break;
						//}
					case HSSFCell.CELL_TYPE_ERROR:
					case HSSFCell.CELL_TYPE_FORMULA:
					default:
						continue;
					}
					lineData.add(data);
				}
				rst.add(lineData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			return null;
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return rst;
    }
    
    private static List<List<String>> xlsxExcelRead2(InputStream in, int sheetNum, int startRow, int startCol, int endCol) throws IllegalStateException{
    	
    	// Excel 파일 불러오기
		List<List<String>> rst = new ArrayList<List<String>>();
		XSSFWorkbook wb = null;

		try {
			// 워크북 오브젝트의 취득
			wb = new XSSFWorkbook(in);
			if (wb.getNumberOfSheets() < 1) {
				return rst;
			}
			// 워크시트를 표시하는 오브젝트의 취득 （1）
			// 데이터시트지정
			XSSFSheet sheet = wb.getSheetAt(sheetNum);
			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			int lastRow = sheet.getLastRowNum();
			//int lastRow = sheet.getPhysicalNumberOfRows() ;
			
			// 행 별로 데이터를 취득
			for (int rowIdx = startRow; rowIdx <= lastRow; rowIdx++) {
				if (rowIdx >= MAX_LINE + startRow) {
					break;
				}
				
				List<String> lineData = new ArrayList<String>();
				// 행을 표시하는 오브젝트의 취득
				XSSFRow row = null;
				try {
					
					row = sheet.getRow(rowIdx);
				} catch (IndexOutOfBoundsException ie) {
					log.debug(ie.toString());
					ie.printStackTrace();
					continue;
				}
			//	rst.add(wb.getSheetName(sheetNum)) ;
				// 행에 데이터가 없는 경우
				if (row == null) {
					break;
				}

				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				short firstCell = (short) startCol;
				short lastCell  = (short) endCol;
				// 셀 별로 데이터를 취득
				for (int cellIdx = firstCell; cellIdx <= lastCell; cellIdx++) {
					String data = "";

					// 셀을 표시하는 오브젝트를 취득
					XSSFCell cell = row.getCell(cellIdx);
					// 빈 셀인 경우
					if (cell == null) {
						lineData.add("");
						continue;
					}
					
					// 셀에 있는 데이터의 종류를 취득
					int type = cell.getCellType();

					// 데이터 종류별로 데이터를 취득
					switch (type) {
					case HSSFCell.CELL_TYPE_BOOLEAN:
						boolean bdata = cell.getBooleanCellValue();
						data = String.valueOf(bdata);
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						if(cell.toString().indexOf("-") != -1){ // 날짜
							data = getFormatDate(cell.getDateCellValue(), "yyyy-MM-dd");
						}else{ // 일반숫자
							int ddata = (int) cell.getNumericCellValue();
							data = String.valueOf(ddata);	
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:
						data = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BLANK:
						//셀의 첫행이 비어있을경우 종료
						//if (cellIdx == firstCell){
							//break addRow;
						//} else {
							data = null;
							break;
						//}
					case HSSFCell.CELL_TYPE_ERROR:
					case HSSFCell.CELL_TYPE_FORMULA:
					default:
						continue;
					}
					lineData.add(data);
				}
				rst.add(lineData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			throw new IllegalStateException("TYPE_ERROR");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return rst;
    }
    
    /**
	 * get Excel's sheet name 
	 * */
	public static String getSheetName(File xFile, int sheetNum){
		String sheetNm = "" ;
		int fNm = xFile.getName().indexOf("xlsx") ;
		
		if( fNm < 0){
			sheetNm = getxlsSheetName(xFile, sheetNum) ;
		}else if( fNm > 0){
			sheetNm = getxlsxSheetName(xFile, sheetNum) ;
		}
		return sheetNm;
	}
    /**
	 * get Excel's sheet name 
	 * */
	private static String getxlsSheetName(File xFile, int sheetNum){
		InputStream in = null;
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		String sheetNm = "" ;
		try {
			in = new FileInputStream(xFile);
			fs = new POIFSFileSystem(in);
			// 워크북 오브젝트의 취득
			wb = new HSSFWorkbook(fs);

			// 워크시트를 표시하는 오브젝트의 취득 （1）
			// 데이터시트지정
			HSSFSheet sheet = wb.getSheetAt(sheetNum);
			sheetNm = sheet.getSheetName() ;
		}catch(Exception e){
			log.debug(e.toString());
			return null;
		}finally{
			log.debug(sheetNm);
		}
		return sheetNm;
	}
	/**
	 * get Excel's sheet name 
	 * */
	private static String getxlsxSheetName(File xFile, int sheetNum){
		InputStream in = null;
		XSSFWorkbook wb = null;
		String sheetNm = "" ;
		try {
			in = new FileInputStream(xFile);
			// 워크북 오브젝트의 취득
			wb = new XSSFWorkbook(in);

			// 워크시트를 표시하는 오브젝트의 취득 （1）
			// 데이터시트지정
			XSSFSheet sheet = wb.getSheetAt(sheetNum);
			sheetNm = sheet.getSheetName() ;
		}catch(Exception e){
			log.debug(e.toString());
			return null;
		}finally{
			log.debug(sheetNm);
		}
		return sheetNm;
	}
	
    /**
     * ECEL파일을 다운로드함
     * 
     * @param csvList 라인리스트
     * @param fileName 파일명
     * @param response  레스폰스
     * @throws Exception
     */
    public static void downLoadFile(HSSFWorkbook wb , String fileName, HttpServletResponse response) throws Exception {
        OutputStreamWriter out = null;
        try {
            if (wb == null) {
                log.error("엑셀 파일 다운로드에 실패했습니다.");
                throw new NullPointerException("");
            }

            if (StringUtils.isEmpty(fileName)) {
                log.error("엑셀 파일 다운로드에 실패했습니다.");
                throw new NullPointerException("");
            }


            fileName = new String(fileName.getBytes("EUC-KR"), "ISO-8859-1");

            //클라이언트 리스펀스 설정
 
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls" );
            response.setContentType("application/vnd.ms-excel");
            out = new OutputStreamWriter(response.getOutputStream(), "EUC-KR");
            
            wb.write(response.getOutputStream()); // 엑셀파일을 출력.


        } catch (Exception e){
        	log.debug(e.toString());
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }
    
    /**
     * fileName 은 was의 루트 경로를 기준으로한다.
     * 
     */
    public static void downLoadFile(String fileName, HttpServletResponse response, String filePath, List<String[]> dataList , int startRow, int startCol) throws Exception {
        downLoadFile(excelTempleteWrite(filePath, dataList, startRow, startCol) ,fileName , response );
    }
    
	/**
	 * 템플릿 파일을 읽어들여, 해당데이터를 써넣고 출력한다.
	 * 
	 * @param 템플릿파일 풀패스 (ex: ) 
	 * @param 시트에 쓰여질 데이터
	 * @param 대상시트 이름 :파라메터에서 주어지는 이름과 템플릿 파일의 시트명이 동일해야함
	 * @param 데이터 쓰기 시작행 번호
	 * @param 데이터 쓰기 시작열 번호
	 * @return 엑셀파일
	 */
	static public HSSFWorkbook excelTempleteWrite(String filePath, List<String[]> dataList , int startRow, int startCol) throws Exception {
		
		POIFSFileSystem filein = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		try {
	        // 템플릿이 될 엑셀파일을 읽습니다.
			filein = new POIFSFileSystem(new FileInputStream(filePath)); // 템플릿 파일 패스를 설정.
			
	        //워크북을 읽어냅니다.
	        wb = new HSSFWorkbook(filein);
	        //시트를 읽어냅니다.
	        sheet = wb.getSheetAt(0); // 시트인덱스, :시트이름으로도 호출 됨
	        String[] tempArray = null;
	        HSSFRow tempRow = null;
	        HSSFCell tempCell = null;
	        HSSFRichTextString  str = null;
	        
	        for(int i = 0; i < dataList.size(); i++  ) {

	        	tempRow = sheet.createRow(i+startRow); // 행시작영
	        	
	        	tempArray = dataList.get(i);
	        	
	        	for (int j = 0; j < tempArray.length; j++ ) {
	        		tempCell = tempRow.createCell(j + startCol);
	        		str = new HSSFRichTextString(tempArray[j]);
	        		tempCell.setCellValue(str);
	        	}
	        }
	        
	        return wb;
        
		} catch (Exception e) {
			log.debug(e.toString());
			return null;
		} finally {
		    if (null != sheet) {
		        filein = null;
		    }
            if (null != sheet) {
                sheet  = null;
            }
		}
	}
	
	public static Workbook createWorkbook(InputStream inp) throws IOException, InvalidFormatException {
        // If clearly doesn't do mark/reset, wrap up
        if(! inp.markSupported()) {
                inp = new PushbackInputStream(inp, 8);
        }

        if(POIFSFileSystem.hasPOIFSHeader(inp)) {
                return new HSSFWorkbook(inp);
        }
        if(POIXMLDocument.hasOOXMLHeader(inp)) {
                return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
	}
	
	/**
	 * Data 날짜를 원하는 String 형식으로 변경
	 * 
	 * @param date
	 *            : 날짜형
	 * @param format
	 *            : 형식 ex)'yyyy-mm-dd'
	 * @return String : 형식의 결과값값
	 */
	public static String getFormatDate(java.util.Date date, String format) {
		if (date == null || format == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
