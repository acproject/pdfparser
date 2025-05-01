package com.owiseman.pdf.utils;

import java.awt.image.BufferedImage;

public class CustomImageCropper {

    /**
     * 从起点 (x,y) 开始，截取最大宽度为 w、最大高度为 h 的子图（自动处理边界）
     * @param source 原图
     * @param x 起始列坐标
     * @param y 起始行坐标
     * @param maxWidth 允许的最大宽度
     * @param maxHeight 允许的最大高度
     * @return 独立的新图像，或 null（参数完全无效时）
     */
    public static BufferedImage cropWithMaxSize(
        BufferedImage source,
        int x,
        int y,
        int maxWidth,
        int maxHeight
    ) {
        // 获取原图尺寸
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();

        // 1. 修正起始点坐标（不能为负数）
        int safeX = Math.max(0, x-30);
        int safeY = Math.max(0, y-46);

        // 2. 计算实际可用的宽度和高度
        int remainingWidth = srcWidth - safeX;  // 原图剩余宽度
        int remainingHeight = srcHeight - safeY; // 原图剩余高度

        int safeWidth = Math.max(0, Math.min(maxWidth, remainingWidth));
        int safeHeight = Math.max(0, Math.min(maxHeight, remainingHeight));

        // 3. 如果修正后的尺寸无效，返回 null
        if (safeWidth <= 0 || safeHeight <= 0) return null;

        // 4. 创建独立的新图像
        BufferedImage subImage = new BufferedImage(
            safeWidth,
            safeHeight,
            source.getType()
        );

        // 5. 批量复制像素（优化性能）
        int[] pixels = source.getRGB(
            safeX,
            safeY,
            safeWidth,
            safeHeight,
            null,
            0,
            safeWidth
        );
        subImage.setRGB(0, 0, safeWidth, safeHeight, pixels, 0, safeWidth);

        return subImage;
    }
}