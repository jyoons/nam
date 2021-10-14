package com.namandnam.nni.common.web;

import java.util.Locale;

public class Const {
  
  public static final String VIEW_PATH_FRONT = "front/"; // 사용자 뷰 경로
  public static final String VIEW_PATH_MOBILE = "mobile/"; // 사용자 뷰 경로
  
  public static final String ERROR_VIEW_404 = "error/404"; // 404 에러 뷰
  public static final String ERROR_VIEW_500 = "error/500"; // 500 에러 뷰
  public static final String ERROR_VIEW_403 = "error/403"; // 404 에러 뷰
  
  public static final String KEY_USER_INFO = "USER_INFO"; //sesstion
  public static final String MEMBER_TYPE_CONTRACT = "S005002";
  public static final String MEMBER_TYPE_NORMAL = "S005001";
  public static final String MEMBER_TYPE_TENANT = "S005003"; 
  public static final String MEMBER_TYPE_SECE = "S005004";
  
  public static final String APT_TYPE_SALES = "apartment";
  public static final String APT_TYPE_CONSTRUCT = "construct";
  
  public static final String KEY_RESULT 	= "result";
  public static final String KEY_ITEM 		= "item";
  public static final String KEY_ITEMS 		= "items";
  public static final String KEY_PARAMS 	= "params";
  public static final String KEY_IMG_PC 	= "pcimg";
  public static final String KEY_IMG_MOBILE = "mobileimg";
  public static final String KEY_VIDEO 		= "video";
  
  public static final String ATTACH_PC		= "P";
  public static final String ATTACH_MOBILE	= "M";
  public static final String ATTACH_VIDEO	= "V";
  
 
  public static final String YYYYMMDD = "yyyy-MM-dd"; // 기본 날짜 포맷
  
  public static final Locale DEFAULT_LOCALE = Locale.KOREA;
  
  // 페이징 옵션
  public static final Integer DEFAULT_CURRENT_PAGE_NO = 1; // 현재 페이지 번호 디폴트 값
  public static final Integer DEFAULT_RECORD_COUNT_PER_PAGE = 10; // 한 페이지당 게시되는 게시물 건 수 디폴트 값
  public static final Integer DEFAULT_PAGE_SIZE = 10; // 페이징의 페이지 번호 개수 디폴트 값
  
  // file max size
  long FILE_MAX_SIZE = 5 * (1024 * 1024);

  Integer LIMIT_DAY = 90;
  
//정규식
  public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

}
