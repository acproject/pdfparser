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
                  [{'type': 'plain text', 'coordinates': [101, 261, 579, 559], 'confidence': 0.985647439956665}, {'type': 'plain text', 'coordinates': [612, 111, 1091, 459], 'confidence': 0.9819751381874084}, {'type': 'plain text', 'coordinates': [611, 461, 1091, 1434], 'confidence': 0.9811663031578064}, {'type': 'plain text', 'coordinates': [101, 110, 579, 259], 'confidence': 0.9807797074317932}, {'type': 'plain text', 'coordinates': [101, 636, 579, 1284], 'confidence': 0.9799904823303223}, {'type': 'plain text', 'coordinates': [101, 1286, 579, 1434], 'confidence': 0.977400004863739}, {'type': 'title', 'coordinates': [101, 583, 524, 610], 'confidence': 0.9344378113746643}, {'type': 'abandon', 'coordinates': [100, 64, 538, 82], 'confidence': 0.8837383389472961}, {'type': 'abandon', 'coordinates': [1036, 1476, 1090, 1493], 'confidence': 0.84259432554245}]
                    """;
            List<PdfBlock> blocks = JsonToPdfBlockParser.parse(jsonText.replace("'", "\""));
           
            try {
                PdfToMarkdownConverter converter = new PdfToMarkdownConverter();
           
                PdfBlockDto pdfBlockDto = converter.convert("assert/2024-Aligning Large Language Models with Humans.pdf", blocks, 2);
              
                Files.write(Paths.get("src/main/resources/output.md"), pdfBlockDto.getMarkdownString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}