package com.owiseman.pdf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.owiseman.pdf.model.PageLayout;


import java.util.List;

public class PdfLayoutDto {
    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("pages_layout")
    private List<PageLayout> PagesLayout;

    public PdfLayoutDto(Integer totalPages, List<PageLayout> pagesLayout) {
        this.totalPages = totalPages;
        PagesLayout = pagesLayout;
    }

    public PdfLayoutDto() {
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<PageLayout> getPagesLayout() {
        return PagesLayout;
    }

    public void setPagesLayout(List<PageLayout> pagesLayout) {
        PagesLayout = pagesLayout;
    }
}




