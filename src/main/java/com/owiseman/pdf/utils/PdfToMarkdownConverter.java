package com.owiseman.pdf.utils;

import com.owiseman.pdf.PdfBlock;
import com.owiseman.pdf.dto.PdfBlockDto;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class PdfToMarkdownConverter {
    public PdfBlockDto convert(String pdfPath, List<PdfBlock> blocks) throws IOException {
        StringBuilder md = new StringBuilder();
        PdfBlockDto pdfBlockDto =  new PdfBlockDto();
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(Paths.get(pdfPath).toFile()));) {
            pdfBlockDto.setDocument(document);
            var sortedBlocks =    sortBlocks(blocks, document.getPage(0).getMediaBox().getWidth());
            for (PdfBlock block : sortedBlocks) {
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
        pdfBlockDto.setMarkdownString(md.toString());
        return pdfBlockDto;
    }

    private List<PdfBlock> sortBlocks(List<PdfBlock> blocks, float pageCenterX) {

        blocks.sort((block1, block2) -> {
            double[] coords1 = block1.getCoordinates();
            double[] coords2 = block2.getCoordinates();
            
            // 判断块是在左侧还是右侧
            // 如果块的x坐标加宽度超过中心，则认为是左侧块
            boolean isBlock1Left = coords1[0] + coords1[2] > pageCenterX;
            boolean isBlock2Left = coords2[0] + coords2[2] > pageCenterX;
            
            // 如果两个块都在左侧或都在右侧，则按y坐标升序排序
            if (isBlock1Left == isBlock2Left) {
                return Double.compare(coords1[1], coords2[1]);
            }
            
            // 如果一个在左侧一个在右侧，左侧的排在前面
            return isBlock1Left ? -1 : 1;
        });
        return blocks;
      
    }
}
