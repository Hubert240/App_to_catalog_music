package com.catalog.catalog.rest.dto;

import com.catalog.catalog.model.User;

public record AudioSummaryDto (
    Long id,
    String title,
    String artist,
    User user,
    String track,
    String album,
    int year,
    String genre,
    String comment,
    String lyrics,
    String composer,
    String publisher,
    String originalArtist,
    String albumArtist,
    String copyright,
    String url,
    String encoder
) {
    }