package com.owiseman.pdf.utils;

import com.owiseman.pdf.Enum.Alignment;
import com.owiseman.pdf.PdfBlock;
import com.owiseman.pdf.dto.PdfBlockDto;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.util.Matrix;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.Rectangle;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class PdfToMarkdownConverter {
    private static final int COMPENSATION_VALUE = 72;
    public PdfBlockDto convert(PDDocument document, List<PdfBlock> blocks, int pageNumber) {
        StringBuilder md = new StringBuilder();
        PdfBlockDto pdfBlockDto = new PdfBlockDto();
        try {
            pdfBlockDto.setDocument(document);
            var sortedBlocks = sortBlocks(blocks, document.getPage(0).getMediaBox().getWidth());
            for (PdfBlock block : sortedBlocks) {
                // 按页面号匹配（假设 blocks 已按页面分组）
                PDPage page = document.getPage(pageNumber); // 示例仅处理第一页
                block.extractTextFromPage(page);
                String titleLevel = "";
                // 生成 Markdown
                switch (block.getType()) {
                    case "title" -> {
                        if (block.getHeight() > 32)
                            titleLevel = "# ";
                        else if (block.getHeight() >= 24 && block.getHeight() < 32)
                            titleLevel = "## ";
                        else if (block.getHeight() < 24 && block.getHeight() >= 16)
                            titleLevel = "### ";
                        else if (block.getHeight() < 16 && block.getHeight() >= 8)
                            titleLevel = "#### ";
                        else if (block.getHeight() < 8)
                            titleLevel = "##### ";
                        if (block.getExtractedText().contains("\r") || block.getExtractedText().contains("\n")) {
                            var extStr = block.getExtractedText().replaceAll("\r", " ");
                            extStr = extStr.replaceAll("\n", " ");
                            extStr = extStr.replaceAll(" {2}", " ");
                            md.append(titleLevel).append(extStr).append("\n");
                        } else {
                            md.append(titleLevel).append(block.getExtractedText()).append("\n\n");
                        }
                    }

                    case "plain text", "figure_caption" -> {
                        md.append(block.getExtractedText()).append("\n\n");
                    }

                    case "table" -> {
                        md.append(pdfBoxTableExtractor(block, pdfBlockDto.getDocument(), pageNumber))
                                .append("\n\n");
                    }
                    case "isolate_formula" -> {
                        md.append(extractAnyAreaToImage(block, pdfBlockDto.getDocument(), pageNumber))
                                .append("\n\n");
                    }

                    case "figure" -> {
                        md.append(extractImageInArea(block, page))
                                .append("\n\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfBlockDto.setMarkdownString(md.toString());
        return pdfBlockDto;
    }

    // 转换非图片的块
    public PdfBlockDto convertNoneImg(PDDocument document, List<PdfBlock> blocks, int pageNumber) {
        StringBuilder md = new StringBuilder();
        PdfBlockDto pdfBlockDto = new PdfBlockDto();
        try {
            pdfBlockDto.setDocument(document);
            var sortedBlocks = sortBlocks(blocks, document.getPage(0).getMediaBox().getWidth());
            for (PdfBlock block : sortedBlocks) {
                // 按页面号匹配（假设 blocks 已按页面分组）
                PDPage page = document.getPage(pageNumber); // 示例仅处理第一页
                block.extractTextFromPage(page);
                String titleLevel = "";
                // 生成 Markdown
                switch (block.getType()) {
                    case "title" -> {
                        if (block.getHeight() > 32)
                            titleLevel = "# ";
                        else if (block.getHeight() >= 24 && block.getHeight() < 32)
                            titleLevel = "## ";
                        else if (block.getHeight() < 24 && block.getHeight() >= 16)
                            titleLevel = "### ";
                        else if (block.getHeight() < 16 && block.getHeight() >= 8)
                            titleLevel = "#### ";
                        else if (block.getHeight() < 8)
                            titleLevel = "##### ";
                        if (block.getExtractedText().contains("\r") || block.getExtractedText().contains("\n")) {
                            var extStr = block.getExtractedText().replaceAll("\r", " ");
                            extStr = extStr.replaceAll("\n", " ");
                            extStr = extStr.replaceAll(" {2}", " ");
                            md.append(titleLevel).append(extStr).append("\n");
                        } else {
                            md.append(titleLevel).append(block.getExtractedText()).append("\n\n");
                        }
                    }

                    case "plain text", "figure_caption" -> {
                        md.append(block.getExtractedText()).append("\n\n");
                    }

                    case "table" -> {
                        md.append(pdfBoxTableExtractor(block, pdfBlockDto.getDocument(), pageNumber))
                                .append("\n\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfBlockDto.setMarkdownString(md.toString());
        return pdfBlockDto;
    }

    public PdfBlockDto convert(String pdfPath, List<PdfBlock> blocks, int pageNumber) throws IOException {
        StringBuilder md = new StringBuilder();
        PdfBlockDto pdfBlockDto = new PdfBlockDto();
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(Paths.get(pdfPath).toFile()));) {
            pdfBlockDto.setDocument(document);
            var sortedBlocks = sortBlocks(blocks, document.getPage(0).getMediaBox().getWidth());
            for (PdfBlock block : sortedBlocks) {
                // 按页面号匹配（假设 blocks 已按页面分组）
                PDPage page = document.getPage(pageNumber); // 示例仅处理第一页
                block.extractTextFromPage(page);
                String titleLevel = "";
                // 生成 Markdown
                switch (block.getType()) {
                    case "title" -> {
                        if (block.getHeight() > 32)
                            titleLevel = "# ";
                        else if (block.getHeight() >= 24 && block.getHeight() < 32)
                            titleLevel = "## ";
                        else if (block.getHeight() < 24 && block.getHeight() >= 16)
                            titleLevel = "### ";
                        else if (block.getHeight() < 16 && block.getHeight() >= 8)
                            titleLevel = "#### ";
                        else if (block.getHeight() < 8)
                            titleLevel = "##### ";
                        if (block.getExtractedText().contains("\r") || block.getExtractedText().contains("\n")) {
                            var extStr = block.getExtractedText().replaceAll("\r", " ");
                            extStr = extStr.replaceAll("\n", " ");
                            extStr = extStr.replaceAll(" {2}", " ");
                            md.append(titleLevel).append(extStr).append("\n");
                        } else {
                            md.append(titleLevel).append(block.getExtractedText()).append("\n\n");
                        }
                    }

                    case "plain text", "figure_caption" -> {
                        md.append(block.getExtractedText()).append("\n\n");
                    }

                    case "table" -> {
                        md.append(pdfBoxTableExtractor(block, pdfBlockDto.getDocument(), pageNumber))
                                .append("\n\n");
                    }
                    case "isolate_formula" -> {
                        md.append(pdfBoxTableExtractor(block, pdfBlockDto.getDocument(), pageNumber))
                                .append("\n\n");
                    }

                    case "figure" -> {
                        md.append(extractImageInArea(block, page))
                                .append("\n\n");
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
            boolean isBlock1Left = coords1[0] < pageCenterX;
            boolean isBlock2Left = coords2[0] < pageCenterX;

            // 如果两个块都在左侧或都在右侧，则按y坐标升序排序
            if (isBlock1Left == isBlock2Left) {
                return Double.compare(coords1[1], coords2[1]);
            }

            // 如果一个在左侧一个在右侧，左侧的排在前面
            return isBlock1Left ? -1 : 1;
        });
        return blocks;

    }

    /// ////////// 下面是处理表格的逻辑

    // 每次只处理一个表格
    private Table extractTablesFromPdfPage(PDDocument document, int pageNumber, PdfBlock pdfBlock) {
        ObjectExtractor oe = new ObjectExtractor(document);
        Page pageIterator = oe.extract(pageNumber);
        Rectangle area = new Rectangle(
                (float) pdfBlock.getX(),
                (float) pdfBlock.getY(),
                (float) pdfBlock.getWidth(),
                (float) ((pdfBlock.getHeight())));
        BasicExtractionAlgorithm spreadsheetExtractionAlgorithm = new BasicExtractionAlgorithm();

        Table table = spreadsheetExtractionAlgorithm.extract(pageIterator.getArea(area)).getFirst();
        return table;
    }

    public static String convertTableToMarkdownString(Table table) {
        StringBuilder sb = new StringBuilder();
        List<List<RectangularTextContainer>> rows = table.getRows();
        // 1. 处理表头（这里假设第一行为表头）
        if (!rows.isEmpty()) {
            List<RectangularTextContainer> headerRow = rows.get(0);
            sb.append(buildMergedCell(headerRow));
            sb.append(buildHeaderSeparator(headerRow.size(), Alignment.LEFT));
        }

        // 处理数据
        for (int i = 1; i < rows.size(); i++) {
            sb.append(buildMergedCell(rows.get(i)));
        }
        return sb.toString();
    }

    // 处理表头
    private static String buildHeaderSeparator(int columnCount, Alignment alignment) {
        String symbol = switch (alignment) {
            case LEFT -> ":---";
            case CENTER -> ":---:";
            case RIGHT -> "---:";

        };
        return "|" + String.join("|", Collections.nCopies(columnCount, symbol)) + "|\n";
    }

    // 处理合并单元格
    private static String buildMergedCell(List<RectangularTextContainer> cells) {
        StringBuilder row = new StringBuilder("|");
        for (RectangularTextContainer cell : cells) {
            String text = cell.getText().isEmpty() ? " " : cell.getText(); // 空单元格填充空格
            text = text.replace("|", "\\|");
            row.append(text).append("|");
        }
        return row.append("\n").toString();
    }

    // 自动判断表头行
    private static boolean isHeader(List<RectangularTextContainer> row) {
        return row.stream().anyMatch(cell -> cell.getText().matches(".*[A-Z].*")); // 包含大写字母
    }

    private static String pdfBoxTableExtractor(PdfBlock pdfBlock, PDDocument document, int pageNumber) {
        Rectangle tableArea = new Rectangle(
                (float) pdfBlock.getCoordinates()[0] / 2,
                (float) pdfBlock.getCoordinates()[1] / 2,
                (float) pdfBlock.getCoordinates()[2] / 2,
                (float) ((pdfBlock.getCoordinates()[3] - pdfBlock.getCoordinates()[1]) / 2)
        );
        String tableText = "";
        try {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.addRegion("table", tableArea);
            stripper.extractRegions(document.getPage(pageNumber));
            tableText = stripper.getTextForRegion("table");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tableText;
    }

    /// /////////// 下面的部分是用来处理图片的
    private static String imageToBase64(PDImageXObject image) throws IOException {
        // 获取 BufferedImage 对象
        BufferedImage bufferedImage = image.getImage();

        // 将图片写入字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream); // 使用 PNG 格式保存
        // 写入图片原始数据
        var imageBase64 = Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
        String markdown = String.format(
                "<img src=\"data:image/%s;base64,%s\" width=\"%d\" height=\"%d\">",
                image.getSuffix(), imageBase64, image.getWidth(), image.getHeight()
        );
        return markdown;
    }

    private static boolean isOverlap(CompareRectangle a, CompareRectangle b) {
        // 计算重叠区域的左、右、上、下边界
        int overlapLeft = Math.max(a.x, b.x);
        int overlapRight = Math.min(a.x + a.width, b.x + b.width);
        int overlapTop = Math.max(a.y, b.y);
        int overlapBottom = Math.min(a.y + a.height, b.y + b.height);

        // 计算重叠区域的面积
        int overlapWidth = overlapRight - overlapLeft;
        int overlapHeight = overlapBottom - overlapTop;
        int overlapArea = overlapWidth * overlapHeight;

        // 计算两个矩形的面积
        int areaA = a.width * a.height;
        int areaB = b.width * b.height;

        // 判断重叠比例是否超过90%
        double overlapRatio = (double) overlapArea / Math.min(areaA, areaB);
        return overlapRatio > 0.9;
    }

    static class CompareRectangle {
        int x, y, width, height;

        public CompareRectangle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }


    public static String extractImageInArea(PdfBlock pdfBlock, PDPage page) {
        PDResources resources = page.getResources();
        if (resources == null) {
            return null;
        }
        try {
            Iterable<COSName> xObjectNames = resources.getXObjectNames();
            if (xObjectNames != null) {
                for (COSName name : xObjectNames) {
                    if (resources.isImageXObject(name)) {
                        PDImageXObject image = (PDImageXObject) resources.getXObject(name);
                        var pdfBoxRect = new CompareRectangle(
                                (int) pdfBlock.getX(),
                                (int) pdfBlock.getY(),
                                (int) pdfBlock.getWidth(),
                                (int) pdfBlock.getHeight()
                        );

                        var imageRect = new CompareRectangle(
                                image.getImage().getMinX(),
                                image.getImage().getMinY(),
                                image.getWidth(),
                                image.getHeight()
                        );
                        if (isOverlap(pdfBoxRect, imageRect)) {
                            return imageToBase64(image);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /// /////////// 处理矢量图的部分
    // todo 寻找到更好的工具再进行更新


    /// /////////// 下面的部分是用来处理公式的
    public static String extractAnyAreaToImage(PdfBlock pdfBlock, PDDocument document, int pageNumber) {
        PDPage page = document.getPage(pageNumber);
        PDFRenderer renderer = new PDFRenderer(document);
        try {
            BufferedImage fullImage = renderer.renderImage(pageNumber,2.0f); // 模型放大了2倍（矩阵是2倍）
            // 裁剪图像
            BufferedImage subImage = fullImage.getSubimage((int)pdfBlock.getX()-COMPENSATION_VALUE, (int)(pdfBlock.getY()-pdfBlock.getHeight()-COMPENSATION_VALUE),
                    (int) (pdfBlock.getWidth()-pdfBlock.getX()), (int) (pdfBlock.getHeight()));

            // 将图像转换为字节数组（PNG格式）
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(subImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.encodeBase64String(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
