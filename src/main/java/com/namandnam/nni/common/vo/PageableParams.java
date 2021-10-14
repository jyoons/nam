package com.namandnam.nni.common.vo;

import com.namandnam.nni.common.web.Const;

/**
 * @author CREAM.KSH Alt + Shift + S -> R
 *
 */
public class PageableParams {

  public Integer recordCountPerPage = Const.DEFAULT_RECORD_COUNT_PER_PAGE;
  public Integer pageSize = Const.DEFAULT_PAGE_SIZE;
  public Integer currentPageNo = Const.DEFAULT_CURRENT_PAGE_NO;

  public Integer firstPageNo = 1;
  public Integer prevPageNo;
  public Integer startPageNo;
  public Integer endPageNo;
  public Integer nextPageNo;
  public Integer lastPageNo;
  public Integer totalRecordCount;
  public Integer offsetNo;

  public Integer getRecordCountPerPage() {
    return recordCountPerPage;
  }

  public void setRecordCountPerPage(Integer recordCountPerPage) {
    this.recordCountPerPage = recordCountPerPage;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getCurrentPageNo() {
    return currentPageNo;
  }

  public void setCurrentPageNo(Integer currentPageNo) {
    this.currentPageNo = currentPageNo;
  }

  public Integer getFirstPageNo() {
    return firstPageNo;
  }

  public void setFirstPageNo(Integer firstPageNo) {
    this.firstPageNo = firstPageNo;
  }

  public Integer getPrevPageNo() {
    return prevPageNo;
  }

  public void setPrevPageNo(Integer prevPageNo) {
    this.prevPageNo = prevPageNo;
  }

  public Integer getStartPageNo() {
    return startPageNo;
  }

  public void setStartPageNo(Integer startPageNo) {
    this.startPageNo = startPageNo;
  }

  public Integer getEndPageNo() {
    return endPageNo;
  }

  public void setEndPageNo(Integer endPageNo) {
    this.endPageNo = endPageNo;
  }

  public Integer getNextPageNo() {
    return nextPageNo;
  }

  public void setNextPageNo(Integer nextPageNo) {
    this.nextPageNo = nextPageNo;
  }

  public Integer getLastPageNo() {
    return lastPageNo;
  }

  public void setLastPageNo(Integer lastPageNo) {
    this.lastPageNo = lastPageNo;
  }

  public Integer getTotalRecordCount() {
    return totalRecordCount;
  }

  public void setTotalRecordCount(Integer totalRecordCount) {
    this.totalRecordCount = totalRecordCount;
  }
  
  public Integer getOffsetNo() {
	return (currentPageNo-1) * recordCountPerPage;
  }

  public void paginate() {

    int lastPage = (this.getTotalRecordCount() + (this.getRecordCountPerPage() - 1)) / this.getRecordCountPerPage();

    if (lastPage <= 0) {
      lastPage = 1;
    }

    if (this.getCurrentPageNo() > lastPage) {
      this.setCurrentPageNo(lastPage);
    } else if (getCurrentPageNo() < 0) {
      this.setCurrentPageNo(Const.DEFAULT_CURRENT_PAGE_NO);
    }

    boolean isFirst = this.getCurrentPageNo() == Const.DEFAULT_CURRENT_PAGE_NO;
    boolean isLast = this.getCurrentPageNo() == lastPage;
    int startPage = ((this.getCurrentPageNo() - 1) / this.getPageSize()) * this.getPageSize() + 1;
    int endPage = startPage + this.getPageSize() - 1;
    if (endPage > lastPage) {
      endPage = lastPage;
    }
    this.setFirstPageNo(Const.DEFAULT_CURRENT_PAGE_NO);
    if (isFirst) {
      this.setPrevPageNo(1); // 이전 페이지 번호
    } else {
      this.setPrevPageNo(this.getCurrentPageNo() - 1);
    }
    this.setStartPageNo(startPage);
    this.setEndPageNo(endPage);
    if (isLast) {
      this.setNextPageNo(lastPage);
    } else {
      this.setNextPageNo(this.getCurrentPageNo() + 1);
    }
    this.setLastPageNo(lastPage); // 마지막 페이지 번호

  }

}
