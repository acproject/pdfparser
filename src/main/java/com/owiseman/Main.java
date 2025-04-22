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
                    [{'type': 'plain text', 'coordinates': [109, 487, 576, 794], 'confidence': 0.9844850301742554}, {'type': 'plain text', 'coordinates': [109, 799, 576, 981], 'confidence': 0.9830796718597412}, {'type': 'plain text', 'coordinates': [110, 986, 576, 1107], 'confidence': 0.9805922508239746}, {'type': 'plain text', 'coordinates': [614, 550, 1082, 827], 'confidence': 0.9783114790916443}, {'type': 'plain text', 'coordinates': [615, 456, 1079, 545], 'confidence': 0.9729617834091187}, {'type': 'title', 'coordinates': [109, 456, 430, 482], 'confidence': 0.9532263278961182}, {'type': 'figure_caption', 'coordinates': [109, 1139, 455, 1163], 'confidence': 0.9165716171264648}, {'type': 'abandon', 'coordinates': [368, 56, 818, 76], 'confidence': 0.9140169620513916}, {'type': 'abandon', 'coordinates': [119, 56, 181, 75], 'confidence': 0.7938159704208374}, {'type': 'table_caption', 'coordinates': [108, 112, 459, 135], 'confidence': 0.7721415162086487}, {'type': 'figure_caption', 'coordinates': [615, 858, 988, 881], 'confidence': 0.6703601479530334}, {'type': 'table', 'coordinates': [110, 136, 1078, 431], 'confidence': 0.65052330493927}, {'type': 'table', 'coordinates': [615, 885, 1079, 1105], 'confidence': 0.6009944081306458}, {'type': 'figure', 'coordinates': [615, 885, 1079, 1105], 'confidence': 0.5729338526725769}, {'type': 'table_caption', 'coordinates': [615, 858, 988, 881], 'confidence': 0.5079803466796875}, {'type': 'figure', 'coordinates': [110, 1164, 1079, 1481], 'confidence': 0.48604366183280945}, {'type': 'table', 'coordinates': [110, 1164, 1079, 1481], 'confidence': 0.3295596241950989}, {'type': 'table', 'coordinates': [112, 1371, 1078, 1480], 'confidence': 0.28874385356903076}, {'type': 'abandon', 'coordinates': [82, 1625, 871, 1650], 'confidence': 0.27455514669418335}, {'type': 'figure_caption', 'coordinates': [108, 112, 459, 135], 'confidence': 0.2566336989402771}]
                    """;
            List<PdfBlock> blocks = JsonToPdfBlockParser.parse(jsonText.replace("'", "\""));

            try {
                PdfToMarkdownConverter converter = new PdfToMarkdownConverter();

                PdfBlockDto pdfBlockDto = converter.convert("assert/Ref20.pdf", blocks, 6);


                Files.write(Paths.get("src/main/resources/output.md"), pdfBlockDto.getMarkdownString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}