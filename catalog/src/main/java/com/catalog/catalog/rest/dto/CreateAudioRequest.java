package com.catalog.catalog.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateAudioRequest {

    private String title;

    private String artist;

    private MultipartFile audioFile;

    private MultipartFile coverArt;

    private Long userId;

    private String track;
    private String album;
    private String year;
    private String genre;
    private String comment;
    private String lyrics;
    private String composer;
    private String publisher;
    private String originalArtist;
    private String albumArtist;
    private String copyright;
    private String url;
    private String encoder;
}
