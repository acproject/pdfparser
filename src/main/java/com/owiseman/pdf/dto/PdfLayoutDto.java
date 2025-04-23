package com.owiseman.pdf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.owiseman.pdf.Enum.PdfLayoutType;
import com.owiseman.pdf.model.PagesLayout;

import java.util.List;

public class PdfLayoutDto {
    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("pages_layout")
    private List<PagesLayout> PagesLayout;

    public PdfLayoutDto(Integer totalPages, List<PagesLayout> pagesLayout) {
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

    public List<PagesLayout> getPagesLayout() {
        return PagesLayout;
    }

    public void setPagesLayout(List<PagesLayout> pagesLayout) {
        PagesLayout = pagesLayout;
    }
}




