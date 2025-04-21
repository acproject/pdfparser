package com.owiseman.pdf.dto;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfBlockDto {
    private String markdownString;
    private PDDocument document;

    public PdfBlockDto(String markdownString, PDDocument document) {
        this.markdownString = markdownString;
        this.document = document;
    }

    public PdfBlockDto() {
        
    }

    public String getMarkdownString() {
        return markdownString;
    }

    public PDDocument getDocument() {
        return document;
    }

    public void setMarkdownString(String markdownString) {
        this.markdownString = markdownString;
    }

    public void setDocument(PDDocument document) {
        this.document = document;
    }

}
