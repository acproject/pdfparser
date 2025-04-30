package com.owiseman.pdf.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owiseman.pdf.PdfBlock;
import com.owiseman.pdf.model.Layout;

import java.util.ArrayList;
import java.util.List;

public class JsonToPdfBlockParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<PdfBlock> parse(List<Layout> layoutList) throws Exception {
        List<PdfBlock> pdfBlockList = new ArrayList<>();
        for (Layout layout : layoutList) {
            PdfBlock pdfBlock = new PdfBlock(
                    layout.getType().getType(),
                    new double[]{
                            layout.getCoordinates()[0],
                            layout.getCoordinates()[1],
                            layout.getCoordinates()[2],
                            layout.getCoordinates()[3]},
                    layout.getConfidence()
            );
            pdfBlockList.add(pdfBlock);

        }
        return pdfBlockList;
    }

    public static List<PdfBlock> parseOneBlock(Layout layout) throws Exception {


        PdfBlock pdfBlock = new PdfBlock(
                layout.getType().getType(),
                new double[]{
                        layout.getCoordinates()[0],
                        layout.getCoordinates()[1],
                        layout.getCoordinates()[2],
                        layout.getCoordinates()[3]},
                layout.getConfidence()
        );
        List<PdfBlock> blocks = new ArrayList<>();
        blocks.add(pdfBlock);
        return blocks;
    }

}
