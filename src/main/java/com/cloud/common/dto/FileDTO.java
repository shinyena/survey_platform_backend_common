package com.cloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class FileDTO {
    private String originalName;
    private byte[] fileBytes;
}
