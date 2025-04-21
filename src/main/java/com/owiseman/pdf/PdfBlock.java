package com.owiseman.pdf;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class PdfBlock {
    private String type;
    private double[] coordinates;
    private double confidence;
    private String extractedText;

    public PdfBlock(String type, double[] coordinates, double confidence) {
        this.type = type;
        this.coordinates = coordinates;
        this.confidence = confidence;
    }

    public void extractTextFromPage(PDPage page) throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        // 坐标系转换：左下角原点 → 左上角原点
        double pageHeight = page.getMediaBox().getHeight();
        var pc =  getCoordinates();
        Rectangle2D region = new Rectangle2D.Double(
          pc[0]/2, pc[1]/2, pc[2]/2, (pc[3]-pc[1])/2
        );

        stripper.addRegion("region", region);
        stripper.extractRegions(page);
        this.extractedText = stripper.getTextForRegion("region").trim();
    }

    public double getHeight() {
        return (coordinates[3] - coordinates[1])/2;
    }



    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getExtractedText() {
        return extractedText;
    }
}
