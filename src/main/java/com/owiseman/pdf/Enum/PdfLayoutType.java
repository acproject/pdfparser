package com.owiseman.pdf.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PdfLayoutType {
    PLAIN_TEXT("plain text", "(0, 200, 0)"),
    TITLE("title", "(255, 0, 0)"),
    FIGURE("figure", "(0, 0, 255)"),
    TABLE("table", "(255, 165, 0)"),
    TABLE_CAPTION("table_caption", "(255, 135, 0)"),
    TABLE_FOOTNOTE("table_footnote", "(255, 100, 0)"),
    ISOLATE_FORMULA("isolate_formula", "(128, 0, 128)"),
    LIST("list", "(0, 255, 255)"),
    FIGURE_CAPTION("figure_caption", "(0, 20, 45)"),
    ABANDON("abandon", "(100, 100, 100)"),
    HEADER("header", "(255, 105, 180)"),
    FOOTER("footer", "(100, 149, 237)");

    private final String type;
    private final String color;

    PdfLayoutType(String type, String color) {
        this.type = type;
        this.color = color;
    }

    @JsonCreator
    public static PdfLayoutType fromValue(String value) {
        for (PdfLayoutType type : values()) {
            if (type.type.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public String getColor() {
       return color;
    }
}
