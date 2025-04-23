package com.owiseman.pdf.model;

import com.owiseman.pdf.Enum.PdfLayoutType;

public class Layout {
        private PdfLayoutType type;
        private Integer[] coordinates;
        private Double confidence;

    public Layout(PdfLayoutType type, Integer[] coordinates, Double confidence) {

        this.type = type;
        this.coordinates = coordinates;
        this.confidence = confidence;
    }

    public PdfLayoutType getType() {
        return type;
    }

    public void setType(PdfLayoutType type) {
        this.type = type;
    }

    public Integer[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Integer[] coordinates) {
        this.coordinates = coordinates;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
