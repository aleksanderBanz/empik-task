package org.empik.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComplaintRequest {
    @NotBlank
    private String productId;
    @NotBlank
    private String content;
    @NotBlank
    private String complainant;
}