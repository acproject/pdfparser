package com.owiseman.pdf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.owiseman.pdf.model.PageLayout;


import java.util.List;

public class PdfLayoutDto {
    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("pages_layout")
    private List<PageLayout> PagesLayout;

    @JsonProperty("processing_time")
    private Double processingTime;

    public PdfLayoutDto(Integer totalPages, List<PageLayout> pagesLayout, Double processingTime) {
        this.totalPages = totalPages;
        PagesLayout = pagesLayout;
        this.processingTime = processingTime;
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

    public Double getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Double processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public String toString() {
        return "{" +
                "totalPages:" + totalPages +
                ", PagesLayout: " + "[" + PagesLayout + "]" +
                ", processingTime: " + processingTime +
                '}';
    }
}




