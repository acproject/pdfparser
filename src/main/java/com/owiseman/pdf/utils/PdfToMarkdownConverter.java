package com.owiseman.pdf.utils;

import com.owiseman.pdf.PdfBlock;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class PdfToMarkdownConverter {
    public String convert(String pdfPath, List<PdfBlock> blocks) throws IOException {
        StringBuilder md = new StringBuilder();

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(Paths.get(pdfPath).toFile()));) {
            for (PdfBlock block : blocks) {
                // 按页面号匹配（假设 blocks 已按页面分组）
                PDPage page = document.getPage(0); // 示例仅处理第一页
                block.extractTextFromPage(page);
                String titleLevel = "";
                // 生成 Markdown
                switch (block.getType()) {
                    case "title" -> {
                        if (block.getHeight() > 32  )
                         titleLevel= "# ";
                        else if (block.getHeight() >= 24  && block.getHeight() < 32)
                         titleLevel= "## ";
                        else if (block.getHeight() < 24 && block.getHeight() >= 16)
                         titleLevel= "### ";
                        else if (block.getHeight() < 16 && block.getHeight() >= 8)
                         titleLevel= "#### ";
                        else if (block.getHeight() < 8)
                         titleLevel= "##### ";
                        if (block.getExtractedText().contains("\n")) {
                            md.append(titleLevel).append(block.getExtractedText().replaceAll("\n", " " )).append("\n");
                        }else {
                            md.append(titleLevel).append(block.getExtractedText()).append("\n\n");
                        }
                    }

                    case "plain text" -> {
                        md.append(block.getExtractedText()).append("\n\n");
                    }
                }
            }
        }
        return md.toString();
    }
}
