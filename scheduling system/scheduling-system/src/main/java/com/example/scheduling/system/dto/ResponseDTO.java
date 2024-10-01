package com.example.scheduling.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
//@Data  // Use @Data to automatically generate getters, setters, and constructors
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private int code;
    private String message;
    private Object content;
    private long totalElements; // Include total elements if needed
    private int totalPages; // Include total pages if needed

    // Optional: You may include custom methods if necessary, but @Data handles most needs

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
