package com.owiseman;

import com.owiseman.pdf.PdfBlock;
import com.owiseman.pdf.dto.PdfBlockDto;
import com.owiseman.pdf.utils.JsonToPdfBlockParser;

import com.owiseman.pdf.utils.PdfToMarkdownConverter;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(Paths.get("2024-Aligning Large Language Models with Humans.pdf").toFile()));
            // PDPage page = document.getPage(0);
            // PDRectangle mediaBox = page.getMediaBox();
            // float pageHeight = mediaBox.getHeight();
            // float pageWidth = mediaBox.getWidth();

            // PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            // // coordinates[x, y, width, height]
            // stripper.addRegion("region", new Rectangle2D.Double(101 / 2, 208 / 2, 939 / 2, (281-208)/2));  // x/2 , y/2 , width/2, (height-y)/2
            // stripper.extractRegions(page);
            // String text = stripper.getTextForRegion("region");

            // System.out.println("Page Height: " + pageHeight);
            // System.out.println("Page Width: " + pageWidth); //595.276
            // System.out.println("Text: " + text);
            // System.out.println("h:" + (281-208)/2);
            // [101, 208, 939, 281]
            String jsonText = """
                  [{'type': 'plain text', 'coordinates': [101, 1311, 579, 1409], 'confidence': 0.974492073059082}, {'type': 'figure', 'coordinates': [103, 110, 1086, 1149], 'confidence': 0.973132312297821}, {'type': 'plain text', 'coordinates': [611, 1311, 1091, 1409], 'confidence': 0.9690294861793518}, {'type': 'figure_caption', 'coordinates': [99, 1178, 580, 1279], 'confidence': 0.9675584435462952}, {'type': 'abandon', 'coordinates': [998, 65, 1089, 81], 'confidence': 0.9267799854278564}, {'type': 'abandon', 'coordinates': [101, 1475, 154, 1493], 'confidence': 0.8843650817871094}, {'type': 'figure_caption', 'coordinates': [611, 1178, 1090, 1279], 'confidence': 0.819067120552063}]
                    """;
            List<PdfBlock> blocks = JsonToPdfBlockParser.parse(jsonText.replace("'", "\""));
           
            try {
                PdfToMarkdownConverter converter = new PdfToMarkdownConverter();
           
                PdfBlockDto pdfBlockDto = converter.convert("assert/2024-Aligning Large Language Models with Humans.pdf", blocks, 3);
              
                Files.write(Paths.get("src/main/resources/output.md"), pdfBlockDto.getMarkdownString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}