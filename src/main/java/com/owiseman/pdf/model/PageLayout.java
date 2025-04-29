package com.owiseman.pdf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PageLayout {
    @JsonProperty("page_number")
    private Integer pageNumber;
    // page_size": [
    //                2481,
    //                3296
    //            ],
    @JsonProperty("page_size")
    private Integer[] pageSize;
    @JsonProperty("layout")
    private List<Layout> layoutList;

    public PageLayout(Integer pageNumber, Integer[] pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public PageLayout(Integer pageNumber, Integer[] pageSize, List<Layout> layoutList) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.layoutList = layoutList;
    }

    public PageLayout() {
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer[] getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer[] pageSize) {
        this.pageSize = pageSize;
    }

    public List<Layout> getLayoutList() {
        return layoutList;
    }

    public void setLayoutList(List<Layout> layoutList) {
        this.layoutList = layoutList;
    }
}