package com.owiseman.pdf.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owiseman.pdf.PdfBlock;

import javax.lang.model.element.Element;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonToPdfBlockParser {
     private static final ObjectMapper objectMapper = new ObjectMapper();
      public static List<PdfBlock> parse(String jsonString) throws Exception {
        // 1. 将 JSON 转换为 List<Map>
        List<Map<String, Object>> rawList = objectMapper.readValue(
            jsonString,
            new TypeReference<List<Map<String, Object>>>() {}
        );

        // 2. 转换为 PdfBlock 对象并处理坐标
        var blocks = rawList.stream()
            .map(item -> {
                String type = (String) item.get("type");
                List<Integer> coords = (List<Integer>) item.get("coordinates");
                double confidence = (Double) item.get("confidence");

                double[] processedCoords = new double[] {
                    coords.get(0) ,
                    coords.get(1) ,
                    coords.get(2) ,
                    coords.get(3)
                };

                return new PdfBlock(type, processedCoords, confidence);
            })
            .filter(block -> !"abandon".equals(block.getType())) // 过滤废弃内容
            .collect(Collectors.toList());

            blocks.stream()
            .sorted(Comparator
                .comparingDouble((PdfBlock e) -> e.getCoordinates()[1]) // 按 y1 升序（从上到下）
                .thenComparingDouble(e -> e.getCoordinates()[0])       // 按 x1 升序（从左到右）
            )
            .collect(Collectors.toList());
             blocks.forEach(e -> {
                 double[] coords = e.getCoordinates();
                 System.out.printf("Type: %-10s | Position: [x1=%f, y1=%f]\n", e.getType(), coords[0], coords[1]);
             });
            return blocks;

    }


}
