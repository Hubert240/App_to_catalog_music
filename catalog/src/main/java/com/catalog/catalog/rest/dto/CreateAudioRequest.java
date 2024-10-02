package com.catalog.catalog.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateAudioRequest {



    @NotBlank
    private String title;

    @NotBlank
    private String artist;


    private MultipartFile audioFile;

    private MultipartFile coverArt;

    private Long userId;
}
