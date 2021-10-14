package com.namandnam.nni.common.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Emails {
  
  private String strTitle; // 제목
  private String strMsg; // 내용
  private String strToMail;
  private String strfromMail;
  private String strPath ;
  private String strType;
  private Boolean sendBool;
  
}
