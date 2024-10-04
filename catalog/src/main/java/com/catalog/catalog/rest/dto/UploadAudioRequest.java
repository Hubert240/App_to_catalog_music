package com.catalog.catalog.rest.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadAudioRequest {

    private MultipartFile audioFile;
    private Long userId;
}
