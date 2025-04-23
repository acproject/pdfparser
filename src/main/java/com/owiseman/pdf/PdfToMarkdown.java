package com.owiseman.pdf;

import com.owiseman.pdf.Enum.PdfLayoutType;
import com.owiseman.pdf.dto.PdfBlockDto;
import com.owiseman.pdf.dto.PdfLayoutDto;

import com.owiseman.pdf.model.PagesLayout;
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
                                     Optional<String> userId, Optional<String> projectId) {
        StringBuilder markdownBuilder = new StringBuilder();
        try {
            PdfToMarkdownConverter converter = new PdfToMarkdownConverter();
            int totalPages = pdfLayoutDto.getTotalPages();

            for (PagesLayout pagesLayout : pdfLayoutDto.getPagesLayout()) {
                int pageNumber = pagesLayout.getPageNumber();

                List<PdfBlock> blocks = JsonToPdfBlockParser.parse(pagesLayout.getLayoutList().get(pageNumber).toString());

                PdfBlockDto pdfBlockDto = converter.convert(document, blocks, pageNumber);
                // 如果服务端传递了接口的实现，则执行下面的操作
                if (exChangeData.isPresent()) {
                    if (pagesLayout.getLayoutList().get(pageNumber).getType()!= PdfLayoutType.ABANDON) {
                        switch (pagesLayout.getLayoutList().get(pageNumber).getType()) {
                            case PdfLayoutType.FIGURE -> {
                                exChangeData.get().sendImageToExChangeVector(userId, projectId,  pdfBlockDto.getMarkdownString());
                            }
                            default -> {
                                exChangeData.get().sendTextToExChangeVector(userId, projectId,  pdfBlockDto.getMarkdownString());
                            }
                        }
                    }
                }
                markdownBuilder.append(pdfBlockDto.getMarkdownString());
                return Files.write(Paths.get("/tmp/output.md"), markdownBuilder.toString().getBytes());
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
