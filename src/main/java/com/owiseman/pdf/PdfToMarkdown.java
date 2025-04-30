package com.owiseman.pdf;

import com.owiseman.pdf.Enum.PdfLayoutType;
import com.owiseman.pdf.dto.PdfBlockDto;
import com.owiseman.pdf.dto.PdfLayoutDto;

import com.owiseman.pdf.model.Layout;
import com.owiseman.pdf.model.PageLayout;
import com.owiseman.pdf.utils.JsonToPdfBlockParser;
import com.owiseman.pdf.utils.PdfToMarkdownConverter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PdfToMarkdown {

    public static Path pdfToMarkdown(PDDocument document, PdfLayoutDto pdfLayoutDto, Optional<ExChangeData> exChangeData,
                                     Optional<String> userId, Optional<String> projectId, Optional<String> fileId) {
        StringBuilder markdownBuilder = new StringBuilder();
        try {
            PdfToMarkdownConverter converter = new PdfToMarkdownConverter();
            int totalPages = pdfLayoutDto.getTotalPages();

            for (PageLayout pageLayout : pdfLayoutDto.getPagesLayout()) {
                int pageNumber = pageLayout.getPageNumber();

                List<PdfBlock> blocks = JsonToPdfBlockParser.parse(pageLayout.getLayoutList());
                // 这里完成一页的转换，得到pdfBlockDto

                // 如果服务端传递了接口的实现，则执行下面的操作
                // 如果当前页面有图片则进行下面的处理
                for (var page : pageLayout.getLayoutList()) {
                    if (exChangeData.isPresent()) {
                        // 过滤掉放弃的页面
                        if (page.getType() != PdfLayoutType.ABANDON) {
                            switch (page.getType()) {
                                case PdfLayoutType.FIGURE -> {
                                    var pageblocks = JsonToPdfBlockParser.parse(Arrays.asList(page));
                                    var pageBlocksDto = converter.convert(document, pageblocks, pageNumber);
                                    exChangeData.get().sendImageToExChangeVector(userId, projectId, fileId, pageBlocksDto.getMarkdownString(), Optional.of(page));
                                }
                                case PdfLayoutType.ISOLATE_FORMULA -> {
                                    var pageblocks = JsonToPdfBlockParser.parseOneBlock(page);
                                    var pageBlocksDto = converter.convert(document, pageblocks, pageNumber);
                                    exChangeData.get().sendFormulaToExChangeVector(userId, projectId, fileId, pageBlocksDto.getMarkdownString(), Optional.of(page));
                                }
                            }
                        }
                    }
                }
                PdfBlockDto pdfBlockDto = converter.convertNoneImg(document, blocks, pageNumber);
                if (exChangeData.isPresent()) {
                    exChangeData.get().sendTextToExChangeVector(userId, projectId,
                            fileId, pdfBlockDto.getMarkdownString() ,Optional.empty(), Optional.of(pageLayout.getLayoutList()));
                }
                markdownBuilder.append(pdfBlockDto.getMarkdownString());

            }
            return Files.write(Paths.get("/tmp/output.md"), markdownBuilder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
