package com.owiseman.pdf;

import com.owiseman.pdf.model.Layout;

import java.util.Optional;

public interface ExChangeData {
    /**
     * send text to exchange vector, and insert to database
     */
    default void sendTextToExChangeVector(Optional<String> UserId, Optional<String> projectId, Optional<String> fileId, String text, Optional<Layout> layout) {
    };

    /**
     * @param image base64 encoded image
     */
    default void sendImageToExChangeVector(Optional<String> UserId, Optional<String> projectId, Optional<String> fileId, String image,Optional<Layout> layout) {
    };

    /**
     * @param formula base64 encoded image
     */
     default void sendFormulaToExChangeVector(Optional<String> UserId, Optional<String> projectId, Optional<String> fileId, String formula,Optional<Layout> layout){
     };
}
