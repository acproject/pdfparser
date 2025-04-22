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
                    [
                    {'type': 'plain text', 'coordinates': [612, 1093, 1089, 1416], 'confidence': 0.9803450703620911}, 
                    {'type': 'plain text', 'coordinates': [101, 1045, 578, 1368], 'confidence': 0.9762018322944641}, 
                    {'type': 'plain text', 'coordinates': [612, 993, 1089, 1091], 'confidence': 0.9740895628929138}, 
                    {'type': 'title',      'coordinates': [101, 208, 939, 281], 'confidence': 0.9638832211494446}, 
                    {'type': 'plain text', 'coordinates': [101, 600, 1090, 672], 'confidence': 0.9557760953903198}, 
                    {'type': 'plain text', 'coordinates': [101, 675, 1090, 876], 'confidence': 0.9508273601531982}, 
                    {'type': 'plain text', 'coordinates': [100, 310, 1018, 364], 'confidence': 0.9463964104652405},
                    {'type': 'plain text', 'coordinates': [101, 399, 295, 439], 'confidence': 0.9332128167152405}, 
                    {'type': 'plain text', 'coordinates': [102, 501, 1091, 549], 'confidence': 0.9318161606788635},
                    {'type': 'abandon',     'coordinates': [101, 118, 578, 159], 'confidence': 0.9304633140563965},
                    {'type': 'plain text', 'coordinates': [102, 551, 1091, 598], 'confidence': 0.9255049824714661},
                    {'type': 'abandon',    'coordinates': [100, 66, 397, 106], 'confidence': 0.9230272173881531},
                    {'type': 'abandon',    'coordinates': [100, 1416, 565, 1435], 'confidence': 0.9128049612045288}, 
                    {'type': 'title',      'coordinates': [103, 992, 263, 1019], 'confidence': 0.9083938002586365}, 
                    {'type': 'abandon',    'coordinates': [100, 1475, 356, 1494], 'confidence': 0.9021286368370056}, 
                    {'type': 'title',      'coordinates': [101, 477, 180, 496], 'confidence': 0.8983834385871887}, 
                    {'type': 'abandon',    'coordinates': [1029, 131, 1089, 189], 'confidence': 0.8972524404525757}, 
                    {'type': 'abandon',    'coordinates': [1036, 1476, 1090, 1493], 'confidence': 0.8507747054100037}, 
                    {'type': 'plain text', 'coordinates': [101, 875, 1090, 948], 'confidence': 0.5072240233421326}]
                    """;
            List<PdfBlock> blocks = JsonToPdfBlockParser.parse(jsonText.replace("'", "\""));
           
            try {
                PdfToMarkdownConverter converter = new PdfToMarkdownConverter();
           
                PdfBlockDto pdfBlockDto = converter.convert("assert/2024-Aligning Large Language Models with Humans.pdf", blocks);
              
                Files.write(Paths.get("src/main/resources/output.md"), pdfBlockDto.getMarkdownString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}