package org.empik.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintResponse {
    private Long id;
    private String productId;
    private String content;
    private LocalDateTime createdAt;
    private String complainant;
    private String country;
    private Integer counter;


}