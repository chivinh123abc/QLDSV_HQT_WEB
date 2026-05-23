package com.ptithcm.shared.dto;

public class PaginationDTO {
    private Integer limit;
    private Integer offset;
    private String sort;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fromDate;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date toDate;

    public Integer getLimit() {
        return limit != null ? limit : 10;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset != null ? offset : 0;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public java.util.Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    public java.util.Date getToDate() {
        return toDate;
    }

    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }
}
