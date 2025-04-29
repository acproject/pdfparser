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

                PdfBlockDto pdfBlockDto = converter.convert(document, blocks, pageNumber);
                // 如果服务端传递了接口的实现，则执行下面的操作
                if (exChangeData.isPresent()) {
                    if (pageLayout.getLayoutList().get(pageNumber).getType()!= PdfLayoutType.ABANDON) {
                        switch (pageLayout.getLayoutList().get(pageNumber).getType()) {
                            case PdfLayoutType.FIGURE -> {
                                exChangeData.get().sendImageToExChangeVector(userId, projectId, fileId,  pdfBlockDto.getMarkdownString(),Optional.of(pageLayout.getLayoutList().get(pageNumber)));
                            }
                            case PdfLayoutType.ISOLATE_FORMULA -> {
                                exChangeData.get().sendFormulaToExChangeVector(userId, projectId, fileId,  pdfBlockDto.getMarkdownString(),Optional.of(pageLayout.getLayoutList().get(pageNumber)));
                            }
                            default -> {
                                exChangeData.get().sendTextToExChangeVector(userId, projectId,  fileId, pdfBlockDto.getMarkdownString(),Optional.of(pageLayout.getLayoutList().get(pageNumber)));
                            }
                        }
                    }
                }
                markdownBuilder.append(pdfBlockDto.getMarkdownString());
            }
            return Files.write(Paths.get("/tmp/output.md"), markdownBuilder.toString().getBytes());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
